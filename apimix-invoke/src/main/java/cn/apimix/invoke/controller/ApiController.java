package cn.apimix.invoke.controller;

import cn.apimix.invoke.model.RequestParams;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @Author: Hor
 * @Date: 2024/6/14 21:36
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController {

    @RequestMapping("/*")
    public ResponseEntity<byte[]> invokingApi(@RequestBody RequestParams requestParams) {

        // 请求参数
        Map<String, Object> query = MapUtil.newHashMap();
        requestParams.getQuery().forEach((field -> query.put(field.getName(), field.getValue().toString())));

        // 请求头
        Map<String, String> headers = MapUtil.newHashMap();
        requestParams.getHeader().forEach(field -> headers.put(field.getName(), field.getValue().toString()));
        headers.remove("X-APIMix-Token");


        log.info("请求参数：{}", requestParams);

        // 请求体
        Map<String, Object> body = MapUtil.newHashMap();
        requestParams.getBody().forEach(field -> body.put(field.getName(), field.getValue()));

        HttpRequest request = HttpUtil.createRequest(
                // 请求方式
                Method.valueOf(requestParams.getMethod()),
                // 请求链接
                HttpUtil.urlWithForm(requestParams.getUrl(),
                        query, Charset.defaultCharset(), false)
        );
        request.clearHeaders();
        request.addHeaders(headers);

        HttpResponse execute = request.execute();
        HttpHeaders respHeaders = new HttpHeaders();

        // 响应状态
        int status = execute.getStatus();

        // 30X 重定向适配
        if (execute.getStatus() >= 300 && execute.getStatus() < 400) {
            String header = execute.header(Header.LOCATION);
            respHeaders.setLocation(URI.create(header));
        }

        // 1. 设置Content-Type
        String contentType = execute.header(Header.CONTENT_TYPE);
        if (contentType != null) {
            respHeaders.setContentType(MediaType.parseMediaType(contentType));
        }
        // 2. 保留其他重要头信息（过滤掉不需要的）
        List<String> ignoreHeaders = Arrays.asList("");
        execute.headers().forEach((key, values) -> {
            if (key != null
                    && !ignoreHeaders.contains(key)
                    && !respHeaders.containsKey(key)) {
                respHeaders.addAll(key, values);
            }
        });
        // 3. 直接获取二进制内容
        byte[] bodyBytes = execute.bodyBytes();

        return ResponseEntity
                .status(HttpStatus.valueOf(status))
                .headers(respHeaders)
                .body(bodyBytes);

    }


}
