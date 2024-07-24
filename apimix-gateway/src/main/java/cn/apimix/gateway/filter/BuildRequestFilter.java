package cn.apimix.gateway.filter;

import cn.apimix.gateway.service.RequestRewriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * 调用前置Filter 构建请求信息
 *
 * @Author: Hor
 * @Date: 2024/6/14 14:52
 * @Version: 1.0
 */
@Component
@Slf4j
public class BuildRequestFilter implements GlobalFilter, Ordered {

    @Resource
    private ModifyRequestBodyGatewayFilterFactory modifyRequestBodyFilter;


    @Resource
    private RequestRewriteService requestRewriteService;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 构建请求信息
        return modifyRequestBodyFilter
                .apply(new ModifyRequestBodyGatewayFilterFactory.Config()
                        .setRewriteFunction(byte[].class, byte[].class, requestRewriteService))
                .filter(exchange, chain);
    }


    @Override
    public int getOrder() {
        return 1;
    }


}
