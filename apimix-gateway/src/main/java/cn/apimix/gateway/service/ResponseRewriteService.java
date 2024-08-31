package cn.apimix.gateway.service;

import cn.apimix.common.model.InterfaceLog;
import cn.apimix.common.service.InnerInterfaceService;
import cn.apimix.gateway.exception.BusinessException;
import cn.apimix.gateway.utils.NetUtils;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

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

            InterfaceLog interfaceLog = serverWebExchange.getAttribute("InterfaceLog");

            interfaceLog.setStatus(response.getStatusCode().value() == 200 ? "成功" : "失败");
            interfaceLog.setResponseHeaders(JSONUtil.parseObj(response.getHeaders(), false).toStringPretty());

            String originalResponseContentType = serverWebExchange.getAttribute(ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);

            if (Objects.equals(response.getStatusCode(), HttpStatus.OK)
                    && StringUtils.isNotBlank(originalResponseContentType)
                    && originalResponseContentType.contains("application/json")) {
                interfaceLog.setResponseBody(JSONUtil.parseObj(bytes, false).toStringPretty());
            } else {
                interfaceLog.setResponseBody("只记录类型为application/json的内容");
            }


            // 响应时间
            interfaceLog.setResponseTime(DateTime.now());


            // 添加请求日志ID
            serverWebExchange.getResponse().getHeaders()
                    .add("ApiMix-Id",request.getId());

            serverWebExchange.getResponse().getHeaders().remove("Access-Control-Allow-Origin");
            serverWebExchange.getResponse().getHeaders().add("Access-Control-Allow-Origin","*");
            // 请求的接口ID
            Long apiId = NetUtils.convertApiId(request);
            // 获取Token信息
            String token = request.getHeaders().getFirst("X-APIMix-Token");

            // 结束时间
            interfaceLog.setEndTime(DateTime.now());


            Boolean invoke = innerInterfaceService.invoke(apiId, token, interfaceLog);


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
