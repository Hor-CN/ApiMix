package cn.apimix.model.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Hor
 * @Date: 2024/8/27 下午10:03
 * @Version: 1.0
 */
@Data
@Builder
public class CaptchaResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 验证码标识
     */
    private String uuid;

    /**
     * 验证码图片（Base64编码，带图片格式：data:image/gif;base64）
     */
    private String img;

    /**
     * 过期时间戳
     */
    private Long expireTime;
}