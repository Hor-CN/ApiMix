package cn.apimix.common.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Hor
 * @Date: 2024/7/25 下午3:29
 * @Version: 1.0
 */
@Data
@Builder
public class InterfaceUser implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long id;

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
     * 用户状态
     */
    private Integer status;

    /**
     * 注册时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
