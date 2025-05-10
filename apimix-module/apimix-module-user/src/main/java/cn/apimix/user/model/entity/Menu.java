package cn.apimix.user.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mybatisflex.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜单实体
 *
 * @Author: Hor
 * @Date: 2024/5/20 19:41
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "menu")
public class Menu implements Serializable {


    /**
     * 菜单ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 菜单标题
     */
    private String title;

    /**
     * 上级菜单ID
     */
    private Long parentId;

    /**
     * 菜单类型（1目录 2菜单 3按钮）
     */
    private Integer type;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件名称
     */
    private String name;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 路由重定向地址
     */
    private String redirect;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 是否外链
     */
    private Boolean isExternal;

    /**
     * 是否缓存
     */
    private Boolean isCache;

    /**
     * 是否隐藏（0否 1是）
     */
    private Boolean isHidden;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 菜单状态（1正常 0禁用）
     */
    private Boolean status;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


    /*
     * 配置能访问该页面的角色['*',"admin"]
     */
    @RelationManyToMany(
            joinTable = "role_menu", // 中间表
            selfField = "id", joinSelfColumn = "menu_id",
            targetTable = "role", valueField = "code",
            targetField = "id", joinTargetColumn = "role_id"
    )
    private List<Role> roles;
    /**
     * 子菜单
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @RelationOneToMany(
            selfField = "id", targetField = "parentId"
    )
    private List<Menu> children;


}
