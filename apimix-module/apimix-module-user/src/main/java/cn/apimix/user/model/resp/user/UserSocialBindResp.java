package cn.apimix.user.model.resp.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 第三方账号绑定信息
 *
 * @Author: Hor
 * @Date: 2024/12/16 19:31
 * @Version: 1.0
 */
@Data
public class UserSocialBindResp implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 来源
     */
    @Schema(description = "来源", example = "GITEE")
    private String source;

    /**
     * 描述
     */
    @Schema(description = "描述", example = "码云")
    private String description;
}
