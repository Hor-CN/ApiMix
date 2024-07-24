package cn.apimix.model.dto.system.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Author: Hor
 * @Date: 2024/5/24 21:45
 * @Version: 1.0
 */
@Data
public class SysUserRetPwdRequest {

    /**
     * 用户ID
     */
    @NotBlank(message = "用户ID不能为空")
    @Pattern(regexp = "\\d+", message = "ID 必须是数字")
    private String id;

    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    private String username;

    /**
     * 新密码
     */
    @NotNull(message = "新密码不能为空")
    private String password;

}
