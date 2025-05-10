package cn.apimix.comment.model.resp;


import cn.apimix.user.model.resp.user.UserInfoResp;
import com.mybatisflex.core.paginate.Page;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: Hor
 * @Date: 2025/1/31 09:03
 * @Version: 1.0
 */
@Data
@Builder
public class CommentResp {

    /**
     * 评论ID
     */
    private Long id;

    /**
     * 父节点
     */
    private Long parentId;

    /**
     * 目标ID
     */
    private Long postId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户信息
     */
    private UserInfoResp user;

    /**
     * 回复给谁
     */
    private Long replyUserId;

    /**
     * 地址
     */
    private String address;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer likes;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 回复
     */
    private Page<CommentResp> reply;

}
