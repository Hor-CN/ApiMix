package cn.apimix.model.dto.user;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @Author: Hor
 * @Date: 2024/5/20 15:43
 * @Version: 1.0
 */
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "账号不能为空")
    private String userName;

    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 16, message = "密码长度为6-16位")
    private String passWord;

}