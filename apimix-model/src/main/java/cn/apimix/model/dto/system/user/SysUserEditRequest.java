package cn.apimix.model.dto.system.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 修改用户请求
 *
 * @Author: Hor
 * @Date: 2024/5/20 19:33
 * @Version: 1.0
 */
@Data
public class SysUserEditRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String userName;

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    private String nickName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

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
    @Size(min = 1, message = "至少分配一个角色")
    private List<String> roles;
}
