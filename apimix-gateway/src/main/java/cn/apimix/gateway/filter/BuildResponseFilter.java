package cn.apimix.gateway.filter;

import cn.apimix.gateway.service.ResponseRewriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.filter.OrderedWebFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @Author: Hor
 * @Date: 2024/6/16 14:50
 * @Version: 1.0
 */
@Slf4j
@Component
public class BuildResponseFilter implements GlobalFilter, Ordered {

    @Resource
    private ModifyResponseBodyGatewayFilterFactory responseBodyGatewayFilterFactory;

    @Resource
    private ResponseRewriteService responseRewriteService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return responseBodyGatewayFilterFactory
                .apply(new ModifyResponseBodyGatewayFilterFactory.Config()
                        .setRewriteFunction(Object.class, Object.class, responseRewriteService))
                .filter(exchange, chain);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
