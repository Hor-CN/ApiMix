package cn.apimix.model.entity;

import cn.apimix.model.enums.RoleTypeEnum;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色实体
 *
 * @Author: Hor
 * @Date: 2024/5/20 19:38
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "role")
public class Role implements Serializable {
    /**
     * 角色ID
     */
    @Id(keyType = KeyType.Auto)
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
