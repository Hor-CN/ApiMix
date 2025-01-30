package cn.apimix.auth.model.resp;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2025/1/29 12:05
 * @Version: 1.0
 */
@Data
@Builder
public class UserInfoResp implements Serializable {
    private static final long serialVersionUID = 1L;

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
     * 角色
     */
    private List<String> roles;

    /**
     * 角色列表
     */
    private List<Integer> roleIds;

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
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
