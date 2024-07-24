package cn.apimix.invoke;

import cn.apimix.common.enums.HttpStatusEnum;
import cn.apimix.common.resp.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Hor
 * @Date: 2024/6/15 17:14
 * @Version: 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 通用异常处理方法
     **/
    @ExceptionHandler(Exception.class)
    public Result<?> error(Exception e, HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestUrl, e);
        return Result.buildFail("节点异常");
    }

}
