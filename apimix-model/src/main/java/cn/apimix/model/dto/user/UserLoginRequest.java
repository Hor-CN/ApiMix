package cn.apimix.model.dto.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @Author: Hor
 * @Date: 2024/3/20 0:24
 * @Version: 1.0
 */
@Data
public class UserLoginRequest implements Serializable {
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
     * 登录设备
     */
    private String device = "unknown";


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
