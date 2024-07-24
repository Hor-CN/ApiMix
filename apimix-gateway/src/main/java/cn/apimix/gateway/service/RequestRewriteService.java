package cn.apimix.gateway.service;

import cn.apimix.gateway.model.ApiParamField;
import cn.apimix.gateway.model.ApiParamTypeEnum;
import cn.apimix.gateway.model.RequestParams;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/6/14 22:57
 * @Version: 1.0
 */
@Service
@Slf4j
public class RequestRewriteService implements RewriteFunction<byte[], byte[]> {
    @Override
    public Publisher<byte[]> apply(ServerWebExchange exchange, byte[] body) {

        ServerHttpRequest request = exchange.getRequest();

        // 构建请求参数
        RequestParams requestParams = RequestParams.builder()
                //http://api.yujn.cn/api/zzxjj.php?type=video
                .url("http://api.yujn.cn/api/zzxjj.php")
                // 请求方式
                .method(request.getMethodValue()).build();
        List<ApiParamField> querys = new ArrayList<>();
        // 请求查询参数
        request.getQueryParams().forEach((k, v) -> {
            ApiParamField field = ApiParamField.builder().name(k).build();

            if (v.size() > 1) {
                field.setType(ApiParamTypeEnum.ARRAY);
            } else {
                if (NumberUtil.isNumber(v.get(0))) {
                    field.setType(ApiParamTypeEnum.NUMBER);
                } else {
                    field.setType(ApiParamTypeEnum.STRING);
                }
            }
            field.setValue(String.join(",", v));
            querys.add(field);
        });

        // 请求头
        List<ApiParamField> headers = new ArrayList<>();
        request.getHeaders().forEach((k, v) -> {
            ApiParamField field = ApiParamField.builder().name(k).value(String.join(",", v)).build();
            if (!"Host".equals(k)) {
                headers.add(field);
            }
        });

        //请求体
        List<ApiParamField> bodys = new ArrayList<>();
        if (body != null) {
            JSONObject originalRequest = JSONUtil.parseObj(body);
            originalRequest.forEach((k, v) -> {
                ApiParamField field = ApiParamField.builder().name(k).value(v).build();
                bodys.add(field);
            });
        }
        requestParams.setQuery(querys);
        requestParams.setHeader(headers);
        requestParams.setBody(bodys);

        JSONObject newRequest = JSONUtil.parseObj(requestParams);
        try {
            log.info("请求调用报文:{}", newRequest);
            return Mono.just(newRequest.toString().getBytes());
        } catch (Exception e) {
            log.error("请求调用时出错", e);
            throw e;
        }
    }
}