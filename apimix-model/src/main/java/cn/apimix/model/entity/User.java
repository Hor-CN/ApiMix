package cn.apimix.model.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户实体
 *
 * @Author: Hor
 * @Date: 2024/5/20 19:40
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "user")
public class User implements Serializable {

    /**
     * 用户ID
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
    private Long id;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

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
     * MD5后的密码
     */
    private String password;

    /**
     * 密码加密盐值
     */
    private String salt;


    /**
     * 是否禁止编辑
     */
    private Boolean disabled;


    /**
     * 逻辑删除标志（0代表存在 1代表删除）
     */
    @Column(isLogicDelete = true)
    private Boolean isDelete;

    /**
     * 角色列表
     */
    @RelationManyToMany(
            joinTable = "user_role", // 中间表
            selfField = "id", joinSelfColumn = "user_id",
            targetField = "id", joinTargetColumn = "role_id"
    )
    private List<Role> roles;

    /**
     * 注册时间
     */
    private Date createTime;


    /**
     * 更新时间
     */
    private Date updateTime;

}