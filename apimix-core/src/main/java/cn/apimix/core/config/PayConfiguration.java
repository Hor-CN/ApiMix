package cn.apimix.core.config;

import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 支付配置
 *
 * @Author: Hor
 * @Date: 2025/1/20 16:55
 * @Version: 1.0
 */

@Configuration
@AllArgsConstructor
public class PayConfiguration {

    @Resource
    private AliPayAccountConfig aliPayAccountConfig;

    @Bean
    public void aliPayApi() {
        AliPayApiConfig aliPayApiConfig = AliPayApiConfig.builder()
                .setAppId(aliPayAccountConfig.getAppId())
                .setAliPayPublicKey(aliPayAccountConfig.getAliPayPublicKey())
                .setCharset("UTF-8")
                .setPrivateKey(aliPayAccountConfig.getPrivateKey())
                .setServiceUrl(aliPayAccountConfig.getSandbox()?"https://openapi-sandbox.dl.alipaydev.com/gateway.do":"https://openapi.alipay.com/gateway.do")
                .setSignType("RSA2")
                .setCertModel(false)
                .build();
        AliPayApiConfigKit.setThreadLocalAliPayApiConfig(aliPayApiConfig);
    }

}
