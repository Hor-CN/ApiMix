package cn.apimix.gateway.service;

import cn.apimix.common.service.InnerInterfaceService;
import cn.apimix.gateway.exception.BusinessException;
import cn.apimix.gateway.utils.NetUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Author: Hor
 * @Date: 2024/6/16 15:06
 * @Version: 1.0
 */
@Service
@Slf4j
public class ResponseRewriteService implements RewriteFunction<byte[], byte[]> {

    @DubboReference
    private InnerInterfaceService innerInterfaceService;


    /**
     * Applies this function to the given arguments.
     *
     * @param serverWebExchange the first function argument
     * @param bytes             the second function argument
     * @return the function result
     */
    @Override
    public Publisher<byte[]> apply(ServerWebExchange serverWebExchange, byte[] bytes) {
        try {
            ServerHttpRequest request = serverWebExchange.getRequest();
            ServerHttpResponse response = serverWebExchange.getResponse();
            log.info("===接口响应===");
            log.info("响应状态：{}",response.getStatusCode());
            log.info("响应类型：{}",response.getHeaders().getContentType());
            log.info("响应头：{}",response.getHeaders());
            log.info("======请求日志(ID:{})结束======", request.getId());

            // 添加请求日志ID
            serverWebExchange.getResponse().getHeaders()
                    .add("ApiMix-Id",request.getId());

            // 请求的接口ID
            Long apiId = NetUtils.convertApiId(request);
            // 获取Token信息
            String token = request.getHeaders().getFirst("X-APIMix-Token");
            Boolean invoke = innerInterfaceService.invoke(apiId, token);
            if (!invoke) {
                throw new BusinessException(400, "接口调用失败");
            }
            return Mono.just(bytes);
        }catch (Exception e) {
            log.error("接口响应出错", e);
            throw e;
        }
    }

}
