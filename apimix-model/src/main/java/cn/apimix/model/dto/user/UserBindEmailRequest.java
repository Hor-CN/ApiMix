package cn.apimix.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户绑定邮箱请求
 *
 * @Author: Hor
 * @Date: 2024/5/20 17:19
 * @Version: 1.0
 */
@Data
public class UserBindEmailRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 邮箱地址
     */
    private String emailAccount;

    /**
     * 邮箱验证码
     */
    private String captcha;
}
