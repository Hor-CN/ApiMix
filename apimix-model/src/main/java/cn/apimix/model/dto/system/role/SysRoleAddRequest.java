package cn.apimix.model.dto.system.role;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 角色添加请求
 *
 * @author Hor
 */
@Data
public class SysRoleAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 角色编号
     */
    @NotBlank(message = "角色编号不能为空")
    private String code;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    private String name;

    /**
     * 角色状态(1启用 0禁用)
     */
    private Integer status;

    /**
     * 角色类型(1内置 2自定义)
     */
    private Integer type;

    /**
     * 角色介绍
     */
    private String description;

}
