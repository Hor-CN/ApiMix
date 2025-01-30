package cn.apimix.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新请求
 *
 * @Author: Hor
 * @Date: 2024/5/20 17:24
 * @Version: 1.0
 */
@Data
public class UserEditRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户描述
     */
    private String description;

}
