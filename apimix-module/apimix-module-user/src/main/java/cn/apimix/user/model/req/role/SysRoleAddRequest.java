package cn.apimix.user.model.req.role;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

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
     * 父子选择
     */
    private Boolean menuCheckStrictly;

    /**
     * 功能列表
     */
    private List<Integer> menuIds;

    /**
     * 角色介绍
     */
    private String description;

}
