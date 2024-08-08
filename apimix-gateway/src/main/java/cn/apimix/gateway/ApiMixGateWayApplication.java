package cn.apimix.gateway;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 网关
 *
 * @Author: Hor
 * @Date: 2024/6/13 14:02
 * @Version: 1.0
 */
@EnableDubbo
@EnableDiscoveryClient
@SpringBootApplication
public class ApiMixGateWayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiMixGateWayApplication.class, args);
    }

}
