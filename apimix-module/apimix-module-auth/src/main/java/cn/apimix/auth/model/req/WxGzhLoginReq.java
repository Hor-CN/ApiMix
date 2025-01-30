package cn.apimix.auth.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @Author: Hor
 * @Date: 2025/1/29 11:22
 * @Version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WxGzhLoginReq extends LoginReq {
    private static final long serialVersionUID = 1L;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String captcha;
}
