package cn.apimix.model.vo.user;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/23 11:43
 * @Version: 1.0
 */
@Data
@Builder
public class UserVo {

    /**
     * 用户ID
     */
    private String id;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像链接
     */
    private String avatar;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户类型（1系统用户 2注册用户）
     */
    private Integer type;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 用户状态
     */
    private Integer status;

    /**
     * 用户描述
     */
    private String description;

    /**
     * 是否禁止编辑
     */
    private Boolean disabled;


    /**
     * 逻辑删除标志（0代表存在 1代表删除）
     */
    private Boolean isDelete;

    /**
     * 角色列表
     */
    private List<String> roles;

    /**
     * 角色的名称
     */
    private List<String> roleNames;

    /**
     * 权限列表
     */
    private List<String> permission;

    /**
     * 注册时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
