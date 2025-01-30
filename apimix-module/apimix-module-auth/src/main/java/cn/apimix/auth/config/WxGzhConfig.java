package cn.apimix.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: Hor
 * @Date: 2025/1/29 13:36
 * @Version: 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "wx")
public class WxGzhConfig {
//    appid: wxbe2c81e45d85a623
//    appsecret: eaf83d388ef9b8c97b16bedb46f2ce07
//    token: ADR2otcIN4jpIIte

    private String appId;
    private String appSecret;
    private String appToken;

}
