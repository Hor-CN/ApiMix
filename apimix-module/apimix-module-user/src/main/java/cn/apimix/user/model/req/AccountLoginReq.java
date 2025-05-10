package cn.apimix.user.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 账号登录参数
 *
 * @Author: Hor
 * @Date: 2025/1/29 11:21
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AccountLoginReq extends LoginReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "账号不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 16, message = "密码长度为6-16位")
    private String password;

    /**
     * 是否记住我
     */
    private Boolean checked = false;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String captcha;

    /**
     * 验证码标识
     */
    @NotBlank(message = "验证码标识不能为空")
    private String uuid;

}
