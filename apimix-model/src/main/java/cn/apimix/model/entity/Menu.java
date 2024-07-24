package cn.apimix.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mybatisflex.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
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
    private Integer id;

    /**
     * 父菜单ID
     */
    private Integer parentId;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 路由重定向
     */
    private String redirect;

    /**
     * 菜单类型（1目录 2菜单 3按钮）
     */
    private Integer type;

    /**
     * 菜单名称
     */
    private String title;

    /**
     * 自定义图标 (比icon优先级高)
     */
    @Column("svgIcon")
    private String svgIcon;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 是否缓存，默认false，页面的name要跟路由的name保持一致才能缓
     */
    @Column("keepAlive")
    private Boolean keepAlive;

    /**
     * 是否隐藏（0否 1是）
     */
    private Boolean hidden;

    /**
     * 显示顺序
     */
    private Integer sort;

    /**
     * 置了该属性进入路由时，则会高亮activeMenu属性对应的侧边栏
     */
    @Column("activeMenu")
    private String activeMenu;

    /**
     * 默认true，如果设置为false，则不会在面包屑中显示
     */
    private Boolean breadcrumb;

    /**
     * 菜单状态（0停用 1启用）
     */
    private Integer status;

    /**
     * 默认true，如果为false，则不会显示在页签中
     */
    @Column("showInTabs")
    private Boolean showInTabs;

    /**
     * 可以设置alwaysShow: true，这样就会忽略之前定义的规则，一直显示根路由
     */
    @Column("alwaysShow")
    private Boolean alwaysShow;

    /**
     * 默认false，如果设置为true，它则会固定在Tab栏中，例如首页
     */
    private Boolean affix;


    /**
     * 路由参数
     */
    private String query;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 创建时间
     */
    private Date createTime;


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
