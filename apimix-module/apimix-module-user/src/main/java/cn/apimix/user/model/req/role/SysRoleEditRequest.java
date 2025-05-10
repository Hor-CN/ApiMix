package cn.apimix.user.model.req.role;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 角色的修改请求
 * @Author: Hor
 * @Date: 2024/5/20 16:57
 * @Version: 1.0
 */
@Data
public class SysRoleEditRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 角色ID
     */
    private Integer id;

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
    private Boolean type;

    /**
     * 菜单ids
     */
    private List<Integer> menuIds;

    /**
     * 菜单父子联动
     */
    private Boolean menuCheckStrictly;


    /**
     * 角色介绍
     */
    private String description;
}
