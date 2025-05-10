package cn.apimix.controller;

import cn.apimix.comment.model.req.CommentReq;
import cn.apimix.comment.model.req.LikedReq;
import cn.apimix.comment.model.resp.CommentResp;
import cn.apimix.comment.service.CommentLikeService;
import cn.apimix.comment.service.CommentService;
import cn.apimix.core.annotation.ResponseResult;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.paginate.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2025/1/31 10:12
 * @Version: 1.0
 */

@RestController
@ResponseResult
@RequestMapping("/api/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @Resource
    private CommentLikeService commentLikeService;


    @GetMapping("/page")
    public Page<CommentResp> page(@Validated CommentReq dto) {
        return commentService.pageByAid(dto);
    }

    @GetMapping("/replyPage")
    public Page<CommentResp> replyPageByPid(CommentReq dto) {
        return commentService.replyPageByPid(dto);
    }

    @SaCheckLogin
    @PostMapping("/save")
    public CommentResp save(@RequestBody CommentReq commentDTO, HttpServletRequest request) {
        return commentService.saveComment(commentDTO,request);
    }

    @DeleteMapping("/remove/{id}")
    public boolean remove(@PathVariable Integer id) {
        return commentService.removeById(id);
    }

    // "评论点赞"
    @SaCheckLogin
    @PostMapping("/liked")
    public Boolean liked(@RequestBody LikedReq likedDTO) {
        long loginIdAsLong = StpUtil.getLoginIdAsLong();
        likedDTO.setUserId(loginIdAsLong);
        return commentLikeService.liked(likedDTO);
    }


   // 查询当前用户的点赞的评论id
    @SaCheckLogin
    @GetMapping("/cidList")
    public List<Long> cidList() {
        return commentLikeService.cidListByUid(StpUtil.getLoginIdAsLong());
    }

}
