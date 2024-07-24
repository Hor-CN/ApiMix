package cn.apimix.core.handler;

import cn.apimix.common.enums.HttpStatusEnum;
import cn.apimix.core.exception.HorApiException;
import cn.apimix.common.resp.Result;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Hor
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
        return Result.buildFail(HttpStatusEnum.INTERNAL_SERVER_ERROR);
    }


    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<?> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                         HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestUrl, e.getMethod());
        return Result.buildFail(HttpStatusEnum.METHOD_NOT_ALLOWED.getCode(), HttpStatusEnum.METHOD_NOT_ALLOWED.getMessage() + e.getMethod());
    }


    //UNSUPPORTED_MEDIA_TYPE
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Result<?> handleHttpMediaTypeNotSupportedException() {
        return Result.buildFail(HttpStatusEnum.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * 断言异常处理
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> handleIllegalArgumentException(IllegalArgumentException exception) {
        log.error("IllegalArgumentException异常{}", Arrays.toString(exception.getStackTrace()));
        return Result.buildFail(HttpStatusEnum.CONFLICT.getCode(), exception.getMessage());
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        log.error("MethodArgumentTypeMismatchException异常{}", Arrays.toString(exception.getStackTrace()));
        return Result.buildFail(HttpStatusEnum.CONFLICT.getCode(), "参数错误");
    }

    /**
     * 数据库完整约束异常
     * @param exception 异常
     * @return result
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result<?> handleDuplicateKeyException(DuplicateKeyException exception) {
        Throwable cause = exception.getCause();
        String message = "操作失败";
        if (cause instanceof SQLIntegrityConstraintViolationException) {
            message = cause.getMessage();
            if (message != null && message.contains("Duplicate entry")) {
                return Result.buildFail(message.split(" ")[2] + "已存在");
            }
        }

        return  Result.buildFail(message);
    }



    @ExceptionHandler(NotLoginException.class)
    public Result<?> handlerNotLoginException(NotLoginException nle) {

        // 判断场景值，定制化异常信息
        String message = "当前会话未登录";
        if (nle.getType().equals(NotLoginException.NOT_TOKEN)) {
            message = "未能读取到有效 token";
        } else if (nle.getType().equals(NotLoginException.INVALID_TOKEN)) {
            message = "token 无效";
        } else if (nle.getType().equals(NotLoginException.TOKEN_TIMEOUT)) {
            message = "token 已过期";
        } else if (nle.getType().equals(NotLoginException.BE_REPLACED)) {
            message = "token 已被顶下线";
        } else if (nle.getType().equals(NotLoginException.KICK_OUT)) {
            message = "token 已被踢下线";
        } else if (nle.getType().equals(NotLoginException.TOKEN_FREEZE)) {
            message = "token 已被冻结";
        } else if (nle.getType().equals(NotLoginException.NO_PREFIX)) {
            message = "未按照指定前缀提交 token";
        }

        // 返回给前端
        return Result.buildFail(HttpStatusEnum.UNAUTHORIZED.getCode(), message);
    }


    /**
     * 权限异常处理方法
     */
    @ExceptionHandler(NotPermissionException.class)
    public Result<?> handleNotPermissionException(NotPermissionException e) {
        log.error("权限异常{}", Arrays.toString(e.getStackTrace()));
        return Result.buildFail(HttpStatusEnum.FORBIDDEN.getCode(),e.getMessage());
    }


    /**
     * 指针异常处理方法
     **/
    @ExceptionHandler(NullPointerException.class)
    public Result<?> error(NullPointerException e) {
        log.error("NPE异常{}", Arrays.toString(e.getStackTrace()));
        return Result.buildFail(HttpStatusEnum.NULL_POINT);
    }

    /**
     * 请求参数错误 HttpMessageNotReadableException
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("请求参数错误{}", Arrays.toString(e.getStackTrace()));
        return Result.buildFail(HttpStatusEnum.PARAM_ERROR);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("请求参数错误{}", Arrays.toString(e.getStackTrace()));
        return Result.buildFail(HttpStatusEnum.PARAM_ERROR);
    }

    /**
     * 参数校验绑定异常 使用@Valid 验证路径中请求实体校验失败后抛出的异常
     */
    @ExceptionHandler(BindException.class)
    public Result<?> bindExceptionHandler(BindException e) {
        String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        return Result.buildFail(HttpStatusEnum.PARAM_ERROR.getCode(), message);
    }

    /**
     * 参数校验绑定异常  使用@Validated 验证路径中 单个参数请求失败抛出异常
     *
     * @param e 异常
     * @return R
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<?> constraintViolationExceptionHandler(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining());
        return Result.buildFail(HttpStatusEnum.PARAM_ERROR.getCode(), message);
    }

    /**
     * 自定义异常处理方法
     *
     * @param e 异常
     * @return R
     */
    @ExceptionHandler(HorApiException.class)
    public Result<?> error(HorApiException e) {
        log.error(e.getMessage(), e);
        return Result.buildFail(HttpStatusEnum.FAIL.getCode(), e.getMessage());
    }

}