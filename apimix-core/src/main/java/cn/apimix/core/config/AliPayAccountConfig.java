package cn.apimix.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里支付宝配置
 *
 * @Author: Hor
 * @Date: 2025/1/20 16:55
 * @Version: 1.0
 */

@Data
@Component
@ConfigurationProperties(prefix = "alipay")
public class AliPayAccountConfig {

    /**
     * appId
     */
    private String appId;
    /**
     * 商户私钥
     */
    private String privateKey;
    /**
     * 支付宝公钥
     */
    private String aliPayPublicKey;
    /**
     * 异步通知url
     */
    private String notifyUrl;

    /**
     * 同步返回的url
     */
    private String returnUrl;

    /**
     * 是否使用沙箱
     */
    private Boolean sandbox = false;

    /**
     * 卖家id
     */
    private String sellerId;

}

