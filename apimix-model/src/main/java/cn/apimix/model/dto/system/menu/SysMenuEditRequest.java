package cn.apimix.model.dto.system.menu;

import cn.apimix.core.annotation.IntegerRange;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 编辑菜单请求
 *
 * @Author: Hor
 * @Date: 2024/5/20 17:56
 * @Version: 1.0
 */
@Data
public class SysMenuEditRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    private Integer id;

    /**
     * 父菜单ID
     */
    private Integer parentId;

    /**
     * 菜单类型（1目录 2菜单 3按钮）
     */
    @IntegerRange(min = 1, max = 3, message = "请选择正确的菜单类型")
    private Integer type;

    /**
     * 自定义图标 (比icon优先级高)
     */
    private String svgIcon;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 菜单名称
     */
    @NotBlank(message = "请输入标题")
    private String title;

    /**
     * 显示顺序
     */
    private Integer sort;

    /**
     * 菜单状态（0停用 1启用）
     */
    private Integer status;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 是否缓存，默认false，页面的name要跟路由的name保持一致才能缓
     */
    private Boolean keepAlive;

    /**
     * 是否隐藏（0否 1是）
     */
    private Boolean hidden;

    /**
     * 路由重定向
     */
    private String redirect;

    /**
     * 默认true，如果设置为false，则不会在面包屑中显示
     */
    private Boolean breadcrumb;

    /**
     * 默认true，如果为false，则不会显示在页签中
     */
    private Boolean showInTabs;

    /**
     * 可以设置alwaysShow: true，这样就会忽略之前定义的规则，一直显示根路由
     */
    private Boolean alwaysShow;

    /**
     * 权限标识
     */
    private String permission;
}
