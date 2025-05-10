package cn.apimix.comment.model.req;

import cn.apimix.core.model.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 新增评论请求
 *
 * @Author: Hor
 * @Date: 2025/1/30 23:16
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CommentReq extends PageRequest {

    // "目标id"
    private Long postId;

    // 评论内容")
    private String content;

    // "评论父id"
    private Long parentId;

    // 回复给谁
    private Long replyUserId;


    public CommentReq(Long current, Long size, Long parentId) {
        this.setPageNumber(current);
        this.setPageSize(size);
        this.parentId = parentId;
    }


}
