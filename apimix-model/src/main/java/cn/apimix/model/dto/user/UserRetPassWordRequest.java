package cn.apimix.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户重置密码请求
 *
 * @Author: Hor
 * @Date: 2024/5/20 17:29
 * @Version: 1.0
 */
@Data
public class UserRetPassWordRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 原密码
     */
    private String oldPassWord;

    /**
     * 新密码
     */
    private String newPassWord;

    /**
     * 验证码
     */
    private String captcha;

}
