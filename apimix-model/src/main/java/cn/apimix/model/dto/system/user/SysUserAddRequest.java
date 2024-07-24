package cn.apimix.model.dto.system.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 添加用户请求
 *
 * @Author: Hor
 * @Date: 2024/5/20 19:28
 * @Version: 1.0
 */
@Data
public class SysUserAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    private String nickname;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户描述
     */
    private String description;

    /**
     * 用户状态
     */
    @NotNull(message = "用户状态不能为空")
    private Integer status;

    /**
     * 角色列表
     */
    @NotNull(message = "角色不能为空")
    @Size(min = 1,message = "至少分配一个角色")
    private List<String> roles;
}
