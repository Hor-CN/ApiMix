package cn.apimix.invoke.controller;

import cn.apimix.invoke.model.RequestParams;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Author: Hor
 * @Date: 2024/6/14 21:36
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController {

    @RequestMapping()
    public ResponseEntity<Object> invokingApi(@RequestBody RequestParams requestParams) {

        // 请求参数
        Map<String, Object> query = MapUtil.newHashMap();
        requestParams.getQuery().forEach((field -> query.put(field.getName(), field.getValue().toString())));


        log.info("被调用");


        // 请求头
        Map<String, String> headers = MapUtil.newHashMap();
        requestParams.getHeader().forEach(field ->
        {
            headers.put(field.getName(), field.getValue().toString());
        });


        // 请求体
        Map<String, Object> body = MapUtil.newHashMap();
        requestParams.getBody().forEach(field -> body.put(field.getName(), field.getValue()));

        HttpRequest request = HttpUtil.createRequest(
                // 请求方式
                Method.valueOf(requestParams.getMethod()),
                // 请求链接
                HttpUtil.urlWithForm(requestParams.getUrl(),
                        query, Charset.defaultCharset(), true)
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

        List<String> ig = Collections.singletonList("Content-Encoding");
        execute.headers().forEach((key, value) -> {
            if (key != null && !ig.contains(key)) {
                respHeaders.add(key, String.join(",", value));
            }
        });
        
        return ResponseEntity
                .status(HttpStatus.valueOf(status))
                .headers(respHeaders)
                .body(execute.body());

    }


}
