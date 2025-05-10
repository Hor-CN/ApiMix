package cn.apimix.comment.service;

import cn.apimix.comment.model.entity.CommentLike;
import cn.apimix.comment.model.req.LikedReq;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 *  服务层。
 *
 * @author Hor
 * @since 2025-01-30
 */
public interface CommentLikeService extends IService<CommentLike> {

    /**
     *  查询用户是否点赞
     * @param likedReq LikedReq
     * @return int 2: mysql点赞 1: redis点赞 0: 不点赞
     */
    int isLiked(LikedReq likedReq);

    /**
     * 评论点赞
     * @param likedReq LikedReq
     */
    boolean liked(LikedReq likedReq);

    /**
     * 获取redis中的评论点赞数量
     * @param commentId commentId
     * @return void
     */
    int getLikeCount(Long commentId);

    /**
     * 根据用户id获取点赞的评论id列表
     * @param uid 用户Id
     * @return List<Long>
     */
    List<Long> cidListByUid(Long uid);

    /**
     * redis同步点赞数量和状态到mysql
     */
    boolean syncLike();

    /**
     * 根据key查询评论点赞key列表
     * @param list uid + cid
     * @return key uid + cid
     */
    List<String> getKeyList(List<String> list);

    /**
     * 批量添加评论点赞
     * @param list list
     * @return boolean
     */
    boolean saveLikeBatch(List<CommentLike> list);

    /**
     * 批量删除评论点赞
     * @param list uid+cid 用户id+评论id key
     */
    boolean removeBatchByCid(List<String> list);

}
