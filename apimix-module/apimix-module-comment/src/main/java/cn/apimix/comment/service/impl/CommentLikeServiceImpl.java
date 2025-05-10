package cn.apimix.comment.service.impl;

import cn.apimix.comment.mapper.CommentLikeMapper;
import cn.apimix.comment.mapper.CommentMapper;
import cn.apimix.comment.model.entity.Comment;
import cn.apimix.comment.model.entity.CommentLike;
import cn.apimix.comment.model.entity.table.CommentLikeTableDef;
import cn.apimix.comment.model.req.LikedReq;
import cn.apimix.comment.service.CommentLikeService;
import cn.apimix.comment.util.RedisUtil;
import cn.apimix.core.utils.ConvertUtils;
import cn.apimix.core.utils.StrUtils;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 服务层实现。
 *
 * @author Hor
 * @since 2025-01-30
 */
@Service
@RequiredArgsConstructor
public class CommentLikeServiceImpl extends ServiceImpl<CommentLikeMapper, CommentLike> implements CommentLikeService {


    private final CommentMapper commentMapper;

    private final RedisUtil redisUtil;


    /**
     * 查询用户是否点赞
     *
     * @param likedReq LikedReq
     * @return int 2: mysql点赞 1: redis点赞 0: 不点赞
     */
    @Override
    public int isLiked(LikedReq likedReq) {
        // 用户id
        String commentLikeKey = getCommentLikeKey(likedReq.getUserId());
        // 查询redis中是否点赞
        Boolean state = redisUtil.sHasKey(commentLikeKey, likedReq.getCommentId());
        if (state) {
            return 1;
        } else {
            // redis中没有点赞，查询redis是否取消点赞
            boolean cancel = redisUtil.sHasKey(commentLikeKey, -likedReq.getCommentId());
            // 如果没取消点赞，查询mysql是否点赞
            if (!cancel) {
                CommentLike commentLike = this.getByLikedReq(likedReq);
                // commentLike不等于null，mysql点赞了
                return commentLike != null ? 2 : 0;
            }
        }
        return 0;
    }

    /**
     * 评论点赞
     *
     * @param likedReq LikedReq
     */
    @Override
    public boolean liked(LikedReq likedReq) {
        String commentLike = getCommentLikeKey(likedReq.getUserId());
        String commentLikeCount = getCommentLikeCountKey(likedReq.getCommentId());
        // 查询用户是否点赞 int 2: mysql点赞 1: redis点赞 0: 不点赞
        int state = this.isLiked(likedReq);
        switch (state) {
            case 2:
                // mysql取消点赞
                redisUtil.execute(tx -> {
                    tx.sSet(commentLike, -likedReq.getCommentId());
                    tx.decr(commentLikeCount);
                });
                break;
            case 1:
                // redis取消点赞
                redisUtil.execute(tx -> {
                    tx.sRemove(commentLike, likedReq.getCommentId());
                    tx.decr(commentLikeCount);
                });
                break;
            case 0:
                // redis点赞
                redisUtil.execute(tx -> {
                    tx.sSet(commentLike, likedReq.getCommentId());
                    tx.incr(commentLikeCount);
                });
                break;
        }
        return true;
    }

    /**
     * 获取redis中的评论点赞数量
     *
     * @param commentId commentId
     * @return void
     */
    @Override
    public int getLikeCount(Long commentId) {
        String commentLikeCount = getCommentLikeCountKey(commentId);
        Integer o = redisUtil.get(commentLikeCount);
        if (o == null) {
            return 0;
        }
        return o;
    }

    /**
     * 根据用户id获取点赞的评论id列表
     *
     * @param uid 用户Id
     * @return List<Long>
     */
    @Override
    public List<Long> cidListByUid(Long uid) {
        List<Long> list = new ArrayList<>();
        // 获取redis中评论点赞评论Id列表
        String commentLikeKey = getCommentLikeKey(uid);
        Set<Long> set = redisUtil.sGet(commentLikeKey, Long.class);
        // 点赞评论id列表
        List<Long> trueCidList = new ArrayList<>();
        // 取消评论点赞评论id列表 redis存储的值: 评论id的负数
        List<Long> falseCidList = new ArrayList<>();
        for (Long cid : set) {
            if (cid > 0) {
                trueCidList.add(cid);
            } else {
                falseCidList.add(-cid);
            }
        }

        // mysql中的评论id
        List<CommentLike> commentLikes = list(query()
                .where(CommentLikeTableDef.COMMENT_LIKE.USER_ID.eq(uid)));

        // 去除mysql在redis中取消点赞对应的评论id
        List<Long> mysqlCidList = commentLikes.stream().map(CommentLike::getCommentId).filter(t -> !falseCidList.contains(t)).collect(Collectors.toList());

        list.addAll(trueCidList);
        list.addAll(mysqlCidList);
        return list;
    }

    /**
     * redis同步点赞数量和状态到mysql
     */
    @Transactional
    @Override
    public boolean syncLike() {

        // 点赞评论列表
        List<CommentLike> trueLikeList = new ArrayList<>();
        // 取消评论点赞评论列表 redis存储的值: 评论id的负数; 取值: uid+cid 用户id+评论id key
        List<String> falseLikeList = new ArrayList<>();
        List<String> keys2 = redisUtil.scan("comment-like:*:comment-id");
        for (String key : keys2) {
            String[] split = key.split(":");
            Long uid = ConvertUtils.toLong(split[1]);
            Set<Long> cList = redisUtil.sGet(key, Long.class);
            for (Long cid : cList) {
                CommentLike commentLike = new CommentLike();
                commentLike.setCommentId(cid);
                commentLike.setUserId(uid);
                if (cid > 0) {
                    trueLikeList.add(commentLike);
                } else {
                    String str = String.format("%d%d", commentLike.getUserId(), Math.abs(commentLike.getCommentId()));
                    falseLikeList.add(str);
                }
                redisUtil.sRemove(key, cid);
            }
        }

        // 评论点赞数量列表
        List<String> keys = redisUtil.scan("comment-like:*:count");
        for (String key : keys) {
            String[] split = key.split(":");
            Long postId = ConvertUtils.toLong(split[1]);
            Integer n = redisUtil.get(key);
            redisUtil.delete(key);
            n = StrUtils.isNull(n, 0);
            Comment comment = new Comment();
            comment.setId(postId);
            comment.setLikes(n);
            //修改点赞数量
            commentMapper.update(comment);
        }


        // mysql取消点赞
        ConvertUtils.batchList(falseLikeList).forEach(this::removeBatchByCid);
        // mysql点赞
        this.saveLikeBatch(trueLikeList);

        return true;
    }

    /**
     * 根据key查询评论点赞key列表
     *
     * @param list uid + cid
     * @return key uid + cid
     */
    @Override
    public List<String> getKeyList(List<String> list) {
        if (StrUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return listAs(query().select(QueryMethods.concat(CommentLikeTableDef.COMMENT_LIKE.USER_ID, CommentLikeTableDef.COMMENT_LIKE.COMMENT_ID))
                        .where(QueryMethods.concat(CommentLikeTableDef.COMMENT_LIKE.USER_ID, CommentLikeTableDef.COMMENT_LIKE.COMMENT_ID).in(list))
                , String.class
        );
    }

    /**
     * 批量添加评论点赞
     *
     * @param list list
     * @return boolean
     */
    @Override
    public boolean saveLikeBatch(List<CommentLike> list) {
        ConvertUtils.batchList(list).forEach(e -> {
            List<String> keys = list.stream().map(CommentLike::getKey).collect(Collectors.toList());
            List<String> keyList = getKeyList(keys);
            List<CommentLike> nodupList = list.stream().filter(t -> !keyList.contains(t.getKey())).collect(Collectors.toList());
            // mysql点赞
            this.saveBatch(nodupList);
        });
        return true;
    }

    /**
     * 批量删除评论点赞
     *
     * @param list uid+cid 用户id+评论id key
     */
    @Override
    public boolean removeBatchByCid(List<String> list) {
        if (StrUtils.isEmpty(list)) {
            return false;
        }
        return remove(query().where(QueryMethods.concat(CommentLikeTableDef.COMMENT_LIKE.USER_ID, CommentLikeTableDef.COMMENT_LIKE.COMMENT_ID).in(list)));
    }


    /**
     * 获取评论点赞key
     *
     * @param uid 用户id
     * @return String
     */
    public String getCommentLikeKey(Long uid) {
        return String.format("comment-like:%d:comment-id", uid);
    }

    /**
     * 获取评论点赞数量key
     *
     * @param commentId 评论id
     * @return String
     */
    public String getCommentLikeCountKey(Long commentId) {
        return String.format("comment-like:%d:count", commentId);
    }


    /**
     * 获取mysql点赞状态
     *
     * @param likedReq likedReq
     * @return com.example.entity.CommentLike
     */
    private CommentLike getByLikedReq(LikedReq likedReq) {
        // 获取mysql中的点赞状态
        return getOne(query()
                .where(CommentLikeTableDef.COMMENT_LIKE.USER_ID.eq(likedReq.getUserId()))
                .and(CommentLikeTableDef.COMMENT_LIKE.COMMENT_ID.eq(likedReq.getCommentId()))
        );
    }

}
