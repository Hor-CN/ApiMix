package cn.apimix.model.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Author: Hor
 * @Date: 2024/10/13 下午8:02
 * @Version: 1.0
 */
@Data
public class WxLoginRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String captcha;
}