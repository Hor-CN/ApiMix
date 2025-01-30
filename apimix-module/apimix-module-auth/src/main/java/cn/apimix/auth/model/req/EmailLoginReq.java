package cn.apimix.auth.model.req;

import cn.hutool.core.lang.RegexPool;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 邮箱登录参数
 *
 * @Author: Hor
 * @Date: 2025/1/29 11:19
 * @Version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EmailLoginReq extends LoginReq {
    private static final long serialVersionUID = 1L;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = RegexPool.EMAIL, message = "邮箱格式错误")
    private String email;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    @Length(max = 6, message = "验证码非法")
    private String captcha;

}
