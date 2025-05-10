package cn.apimix.user.model.resp;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录响应参数
 *
 * @Author: Hor
 * @Date: 2025/1/29 11:26
 * @Version: 1.0
 */
@Data
@Builder
public class LoginResp implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 令牌
     */
    private String token;
}
