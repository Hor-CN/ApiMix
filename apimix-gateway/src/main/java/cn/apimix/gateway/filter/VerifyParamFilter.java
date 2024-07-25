package cn.apimix.gateway.filter;

import cn.apimix.common.service.IUserInterfaceInvokeService;
import cn.apimix.gateway.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 验证参数
 *
 * @Author: Hor
 * @Date: 2024/6/15 21:28
 * @Version: 1.0
 */
@Slf4j
@Component
public class VerifyParamFilter implements Ordered, GlobalFilter {


    private final static String TOKEN = "Token";


    @DubboReference
    private IUserInterfaceInvokeService invokeService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 请求信息
        ServerHttpRequest request = exchange.getRequest();
        // 请求头
        HttpHeaders headers = request.getHeaders();
        // 获取Token信息
        String token = headers.getFirst(TOKEN);
        // 请求头中参数必须完整
        if (token == null) {
            throw new BusinessException(403, "Token不能为空");
        }

        log.info(request.getURI().getPath());
        //todo 获取用户信息
//        invokeService.invoke(,token)

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
