package cn.apimix.model.vo.role;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author: Hor
 * @Date: 2024/5/24 20:30
 * @Version: 1.0
 */
@Data
@Builder
public class RoleVo {

    /**
     * 角色ID
     */
    private Integer id;

    /**
     * 角色编号
     */
    private String code;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色状态(1启用 0禁用)
     */
    private Integer status;

    /**
     * 是否禁用编辑
     */
    private Boolean disabled;

    /**
     * 角色类型(1内置 2自定义)
     */
    private Integer type;

    /**
     * 角色介绍
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createTime;
}
