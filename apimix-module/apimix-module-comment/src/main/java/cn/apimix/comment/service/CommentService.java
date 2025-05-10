package cn.apimix.comment.service;

import cn.apimix.comment.model.entity.Comment;
import cn.apimix.comment.model.req.CommentReq;
import cn.apimix.comment.model.resp.CommentResp;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *  服务层。
 *
 * @author Hor
 * @since 2025-01-30
 */
public interface CommentService extends IService<Comment> {


    /**
     * 添加评论
     *
     * @param commentReq 添加评论
     * @return 评论
     */
    CommentResp saveComment(CommentReq commentReq, HttpServletRequest request);

    /**
     * 根据文章id查询分页评论列表(评论对象里包含回复(reply)的数据)
     *
     * @param commentReq commentReq
     * @return page
     */
    Page<CommentResp> pageByAid(CommentReq commentReq);

    /**
     * 根据parentId分页查询回复
     *
     * @param commentReq commentReq
     * @return page
     */
    Page<CommentResp> replyPageByPid(CommentReq commentReq);

    /**
     * 分页查询评论(评论对象里不包含回复(reply)的数据)
     *
     * @param pageNum  当前页
     * @param pageSize 每页数
     * @return page
     */
    Page<List<Comment>> page(long pageNum, long pageSize);

    /**
     * 批量修改点赞数量
     *
     * @param list 评论列表
     * @return boolean
     */
    void updateBatchById(List<Comment> list);

}
