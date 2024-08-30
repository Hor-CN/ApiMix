package cn.apimix.model.dto.user;

/**
 * @Author: Hor
 * @Date: 2024/8/28 下午8:29
 * @Version: 1.0
 */

import lombok.Data;

@Data
public class ApplyDeveloperRequest {

    private String qq;

    private String email;

    private String website;

    private String captcha;

}
