package cn.apimix.model.dto.user;

import cn.hutool.core.lang.RegexPool;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @Author: Hor
 * @Date: 2024/8/28 下午3:28
 * @Version: 1.0
 */
@Data
public class UserEmailUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 新邮箱
     */
    @NotBlank(message = "新邮箱不能为空")
    @Pattern(regexp = RegexPool.EMAIL, message = "邮箱格式错误")
    private String email;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    @Length(max = 6, message = "验证码非法")
    private String captcha;

    /**
     * 当前密码（加密）
     */
    @NotBlank(message = "当前密码不能为空")
    private String oldPassword;
}