package cn.apimix.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Author: Hor
 * @Date: 2024/6/15 11:51
 * @Version: 1.0
 */
@Slf4j
@Component
public class RequestFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 构建调用请求
        exchange = exchange.mutate().request(build -> {
            build.header("Content-Type", "application/json").build();
            build.method(HttpMethod.POST).build();
        }).build();
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
