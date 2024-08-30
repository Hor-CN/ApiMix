package cn.apimix.gateway.exception;

import cn.apimix.common.resp.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

/**
 * @Author: Hor
 * @Date: 2024/6/15 16:42
 * @Version: 1.0
 */
@Configuration
@Slf4j
@Order(-1)
public class GlobalExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = response.getHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        DataBufferFactory bufferFactory = response.bufferFactory();
        response.setStatusCode(HttpStatus.FORBIDDEN);
        Result<Object> error = Result.buildFail(HttpStatus.FORBIDDEN.value(), ex.getMessage());
        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;
            response.setStatusCode(HttpStatus.valueOf(businessException.getCode()));
            error = Result.buildFail(businessException.getCode(), ex.getMessage());
        }

        log.error("【网关异常】：{}", ex.toString());
        try {
            byte[] errorBytes = objectMapper.writeValueAsBytes(error);
            DataBuffer dataBuffer = bufferFactory.wrap(errorBytes);
            return response.writeWith(Mono.just(dataBuffer));
        } catch (JsonProcessingException e) {
            log.error("JSON序列化异常：{}", e.getMessage());
            return Mono.error(e);
        }
    }
}
