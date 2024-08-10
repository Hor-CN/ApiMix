package cn.apimix.gateway.filter;

import cn.apimix.common.model.InterfaceLog;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static cn.apimix.gateway.filter.CacheBodyGatewayFilter.CACHE_REQUEST_BODY_OBJECT_KEY;
import static cn.apimix.gateway.utils.NetUtils.getIp;
import static cn.apimix.gateway.utils.NetUtils.getPostRequestBody;

/**
 * 全局过滤器
 *
 * @Author: Hor
 * @Date: 2024/6/13 21:23
 * @Version: 1.0
 */
@Component
@Slf4j
public class GatewayGlobalFilter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 日志记录
        ServerHttpRequest request = exchange.getRequest();
        log.info("======请求日志(ID:{})开始======", request.getId());
        log.info("请求路径：{}", request.getPath().pathWithinApplication().value());
        log.info("请求方式：{}", request.getMethod());
        log.info("本机地址：{}", request.getLocalAddress());
        log.info("客户端远程地址：{}", request.getRemoteAddress());
        log.info("接口请求IP：{}", getIp(request));
        log.info("请求参数：{}", request.getQueryParams());
        log.info("请求头：{}", request.getHeaders());
        Object cacheBody = exchange.getAttribute(CACHE_REQUEST_BODY_OBJECT_KEY);

        InterfaceLog interfaceLog = InterfaceLog.builder()
                .requestId(request.getId())
                .requestPath(request.getPath().pathWithinApplication().value())
                .requestMethod(request.getMethodValue())
                .requestParams(JSONUtil.parseObj(request.getQueryParams(), false).toStringPretty())
                .requestHeaders(JSONUtil.parseObj(request.getHeaders(), false).toStringPretty())
                .ip(getIp(request))
                .startTime(DateTime.now())
                .build();

        // 是否拥有请求体
        if (cacheBody != null) {
            @SuppressWarnings("unchecked") String requestBody = getPostRequestBody((Flux<DataBuffer>) cacheBody);
            log.info("请求体：{}", requestBody);
            interfaceLog.setRequestBody(requestBody);
        } else {
            log.info("请求体：{}", "无");
            interfaceLog.setRequestBody("无");
        }

        exchange.getAttributes().put("InterfaceLog", interfaceLog);
        return chain.filter(exchange);
    }


    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}
