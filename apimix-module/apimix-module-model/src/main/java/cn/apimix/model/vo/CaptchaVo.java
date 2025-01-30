package cn.apimix.model.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: Hor
 * @Date: 2024/8/27 下午8:41
 * @Version: 1.0
 */
@Data
@Builder
public class CaptchaVo {



    /**
     * 是否开启验证码
     */
    private Boolean captchaEnabled = true;

    private String uuid;

    /**
     * 验证码图片
     */
    private String img;

}
