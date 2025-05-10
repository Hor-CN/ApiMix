package cn.apimix.comment.service.impl;

import cn.apimix.api.service.ApiReleaseService;
import cn.apimix.comment.mapper.CommentMapper;
import cn.apimix.comment.model.entity.Comment;
import cn.apimix.comment.model.entity.table.CommentTableDef;
import cn.apimix.comment.model.mapstruct.CommentMapping;
import cn.apimix.comment.model.req.CommentReq;
import cn.apimix.comment.model.resp.CommentResp;
import cn.apimix.comment.service.CommentLikeService;
import cn.apimix.comment.service.CommentService;
import cn.apimix.comment.util.IpUtil;
import cn.apimix.user.model.resp.user.UserInfoResp;
import cn.apimix.user.service.UserService;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.extra.servlet.ServletUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论服务层实现。
 *
 * @author Hor
 * @since 2025-01-30
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final CommentMapping commentMapping;

    private final Ip2regionSearcher regionSearcher;

    private final UserService userService;

    private final ApiReleaseService apiReleaseService;


    private final CommentLikeService commentLikeService;

    /**
     * 添加评论
     *
     * @param commentReq 添加评论
     * @return 评论
     */
    @Override
    public CommentResp saveComment(CommentReq commentReq, HttpServletRequest request) {

        Assert.isTrue(apiReleaseService.existsInterfaceById(commentReq.getPostId()), "postId不存在，无法评论");

        // 获取当前登录用户
        Long userId = StpUtil.getLoginIdAsLong();
        Comment comment = commentMapping.commentReqToComment(commentReq);
        comment.setUserId(userId);
        // 获取IP和地理位置
        String clientIp = ServletUtil.getClientIP(request);
        String ipv4Address = IpUtil.getIpv4Address(clientIp, regionSearcher);
        comment.setIp(clientIp);
        comment.setAddress("来自" + ipv4Address);

        // 回复谁
        comment.setReplyUserId(commentReq.getReplyUserId());

        // 保存评论
        save(comment);

        CommentResp commentResp = commentMapping.commentToCommentResp(getById(comment.getId()));
        // 获取当前用户信息
        UserInfoResp currentUser = userService.getUserInfo(userId);
        commentResp.setUser(currentUser);
        return commentResp;
    }


    /**
     * 根据文章id查询分页评论列表(评论对象里包含回复(reply)的数据)
     *
     * @param commentReq commentReq
     * @return page
     */
    @Override
    public Page<CommentResp> pageByAid(CommentReq commentReq) {

        Page<Comment> commentPage = getMapper().paginateWithRelations(
                // 当前页
                commentReq.getPageNumber(),
                // 每页数量
                commentReq.getPageSize(),
                query().where(CommentTableDef.COMMENT.POST_ID.eq(commentReq.getPostId()))
                        // 顶级评论
                        .and(CommentTableDef.COMMENT.PARENT_ID.isNull())
                        // 降序
                        .orderBy(CommentTableDef.COMMENT.CREATE_TIME.desc(), CommentTableDef.COMMENT.ID.desc())
        );

        List<CommentResp> collect = commentPage.getRecords().stream().map(item -> {
            CommentResp commentResp = commentMapping.commentToCommentResp(item);
            UserInfoResp userToUserInfo = userService.getUserToUserInfo(item.getUser());
            commentResp.setUser(userToUserInfo);
            // 合并redis中的点赞数量
            commentResp.setLikes(commentResp.getLikes() + commentLikeService.getLikeCount(commentResp.getId()));
            // 分页查询回复
            Long id = item.getId();
            // 第一页每页第一次差两个
            Page<CommentResp> commentRespPage = replyPageByPid(new CommentReq(1L, 5L, id));
            commentResp.setReply(commentRespPage);

            return commentResp;
        }).collect(Collectors.toList());

        return new Page<>(collect, commentPage.getPageNumber(), commentReq.getPageSize(), commentPage.getTotalRow());
    }

    /**
     * 根据parentId分页查询回复
     *
     * @param commentReq commentReq
     * @return page
     */
    @Override
    public Page<CommentResp> replyPageByPid(CommentReq commentReq) {

        Page<Comment> commentPage = getMapper().paginateWithRelations(
                // 当前页
                commentReq.getPageNumber(),
                // 每页数量
                commentReq.getPageSize(),
                query().where(CommentTableDef.COMMENT.PARENT_ID.eq(commentReq.getParentId()))
                        // 降序
                        .orderBy(CommentTableDef.COMMENT.CREATE_TIME.desc(), CommentTableDef.COMMENT.ID.desc())
        );
        List<CommentResp> replyCollect = commentPage.getRecords().stream().map(reply -> {
            CommentResp replyResp = commentMapping.commentToCommentResp(reply);
            replyResp.setUser(userService.getUserToUserInfo(reply.getUser()));
            // 合并redis中的点赞数量
            replyResp.setLikes(reply.getLikes() + commentLikeService.getLikeCount(replyResp.getId()));
            return replyResp;
        }).collect(Collectors.toList());

        return new Page<>(replyCollect, commentPage.getPageNumber(), commentReq.getPageSize(), commentPage.getTotalRow());

    }

    /**
     * 分页查询评论(评论对象里不包含回复(reply)的数据)
     *
     * @param pageNum  当前页
     * @param pageSize 每页数
     * @return page
     */
    @Override
    public Page<List<Comment>> page(long pageNum, long pageSize) {
        return null;
    }

    /**
     * 批量修改点赞数量
     *
     * @param list 评论列表
     * @return boolean
     */
    @Override
    public void updateBatchById(List<Comment> list) {
        list.forEach(comment -> {
            Comment currentComment = getById(comment.getId());
            // 旧点赞
            Integer oldLikes = currentComment.getLikes();
            // 行锁+状态机 原子操作
            currentComment.setLikes(oldLikes + comment.getLikes());
            update(currentComment, query()
                    .where(CommentTableDef.COMMENT.ID.eq(comment.getId()))
                    .and(CommentTableDef.COMMENT.LIKES.eq(oldLikes))
            );
        });
    }
}
