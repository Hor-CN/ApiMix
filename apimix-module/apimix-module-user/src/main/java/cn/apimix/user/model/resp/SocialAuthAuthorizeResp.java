package cn.apimix.user.model.resp;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 三方账号授权认证响应信息
 *
 * @Author: Hor
 * @Date: 2025/1/29 16:04
 * @Version: 1.0
 */
@Data
@Builder
public class SocialAuthAuthorizeResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 授权 URL
     */
    private String authorizeUrl;
}