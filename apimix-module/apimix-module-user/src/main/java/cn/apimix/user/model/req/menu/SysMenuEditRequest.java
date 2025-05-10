package cn.apimix.user.model.req.menu;

import cn.apimix.core.annotation.IntegerRange;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

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
     * 菜单名称
     */
    @NotBlank(message = "请输入标题")
    private String title;


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
     * 显示顺序
     */
    private Integer sort;

    /**
     * 菜单状态（0停用 1启用）
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

}
