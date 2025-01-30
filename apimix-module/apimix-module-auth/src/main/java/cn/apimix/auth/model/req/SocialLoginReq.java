package cn.apimix.auth.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @Author: Hor
 * @Date: 2025/1/29 11:21
 * @Version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SocialLoginReq extends LoginReq {
    private static final long serialVersionUID = 1L;

    /**
     * 第三方登录平台
     */
    @NotBlank(message = "第三方登录平台不能为空")
    private String source;

    /**
     * 授权码
     */
    @NotBlank(message = "授权码不能为空")
    private String code;

    /**
     * 状态码
     */
    @NotBlank(message = "状态码不能为空")
    private String state;
}

