package cn.apimix.model.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Author: Hor
 * @Date: 2024/8/28 下午2:37
 * @Version: 1.0
 */
@Data
public class UserPasswordUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 当前密码（加密）
     */
    private String oldPassword;

    /**
     * 新密码（加密）
     */
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}
