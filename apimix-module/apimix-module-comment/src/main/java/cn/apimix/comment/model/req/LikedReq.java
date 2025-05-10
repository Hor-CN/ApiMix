package cn.apimix.comment.model.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author: Hor
 * @Date: 2025/2/1 10:57
 * @Version: 1.0
 */
@Data
public class LikedReq {

    // "评论id"
    private Long commentId;

    //    "用户id"
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long userId;

}
