package cn.apimix.common.resp;


import cn.apimix.common.enums.HttpStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一的响应
 * @Author: Hor
 * @Date: 2023/5/22 22:40
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
//设置属性返回顺序
@JsonPropertyOrder({"code", "success", "message", "data"})
public class Result<T> {
    /**
     * 响应状态码 code
     */
    private Integer code;

    /**
     * 是否响应成功 状态信息
     */
    private Boolean success;

    /**
     * 信息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;


    /**
     * 全参数方法
     *
     * @param code    状态码
     * @param status  状态
     * @param message 返回信息
     * @param data    返回数据
     * @param <T>     泛型
     * @return {@link Result<T>}
     */
    private static <T> Result<T> build(Integer code, Boolean status, String message, T data) {
        Result<T> responseResult = new Result<>();
        responseResult.setCode(code);
        responseResult.setSuccess(status);
        responseResult.setMessage(message);
        responseResult.setData(data);
        return responseResult;
    }

    /**
     * 全参数方法
     *
     * @param code    状态码
     * @param status  状态
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link Result<T>}
     */
    private static <T> Result<T> build(Integer code, Boolean status, String message) {
        Result<T> responseResult = new Result<>();
        responseResult.setCode(code);
        responseResult.setSuccess(status);
        responseResult.setMessage(message);
        return responseResult;
    }


    /**
     * 成功返回（无参）
     *
     * @param <T> 泛型
     * @return {@link Result<T>}
     */
    public static <T> Result<T> buildSuccess() {
        return build(
                HttpStatusEnum.SUCCESS.getCode(),
                true,
                HttpStatusEnum.SUCCESS.getMessage(),
                null
        );
    }

    /**
     * 成功返回（枚举参数）
     *
     * @param httpResponseEnum 枚举参数
     * @param <T>              泛型
     * @return {@link Result<T>}
     */
    public static <T> Result<T> buildSuccess(HttpStatusEnum httpResponseEnum) {
        return build(
                httpResponseEnum.getCode(),
                true,
                httpResponseEnum.getMessage());
    }

    /**
     * 成功返回（状态码+返回信息）
     *
     * @param code    状态码
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link Result<T>}
     */
    public static <T> Result<T> buildSuccess(Integer code, String message) {
        return build(
                code,
                true,
                message
        );
    }


    /**
     * 成功返回（返回信息 + 数据）
     *
     * @param message 返回信息
     * @param data    数据
     * @param <T>     泛型
     * @return {@link Result<T>}
     */
    public static <T> Result<T> buildSuccess(String message, T data) {
        return build(
                HttpStatusEnum.SUCCESS.getCode(),
                true,
                message,
                data
        );
    }

    /**
     * 成功返回（状态码+返回信息+数据）
     *
     * @param code    状态码
     * @param message 返回信息
     * @param data    数据
     * @param <T>     泛型
     * @return {@link Result<T>}
     */
    public static <T> Result<T> buildSuccess(Integer code, String message, T data) {
        return build(
                code,
                true,
                message,
                data
        );
    }

    /**
     * 成功返回（数据）
     *
     * @param data 数据
     * @param <T>  泛型
     * @return {@link Result<T>}
     */
    public static <T> Result<T> buildSuccess(T data) {
        return build(
                HttpStatusEnum.SUCCESS.getCode(),
                true,
                HttpStatusEnum.SUCCESS.getMessage(),
                data
        );
    }

    /**
     * 成功返回（返回信息）
     *
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link Result<T>}
     */
    public static <T> Result<T> buildSuccess(String message) {
        return build(
                HttpStatusEnum.SUCCESS.getCode(),
                true,
                message,
                null
        );
    }


    /**
     * 失败返回（无参）
     *
     * @param <T> 泛型
     * @return {@link Result<T>}
     */
    public static <T> Result<T> buildFail() {
        return build(
                HttpStatusEnum.INTERNAL_SERVER_ERROR.getCode(),
                false,
                HttpStatusEnum.INTERNAL_SERVER_ERROR.getMessage(),
                null
        );
    }


    /**
     * 失败返回（枚举）
     *
     * @param httpResponseEnum 枚举
     * @param <T>              泛型
     * @return {@link Result<T>}
     */
    public static <T> Result<T> buildFail(HttpStatusEnum httpResponseEnum) {
        return build(
                httpResponseEnum.getCode(),
                false,
                httpResponseEnum.getMessage()
        );
    }


    /**
     * 失败返回（状态码+返回信息）
     *
     * @param code    状态码
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link Result<T>}
     */
    public static <T> Result<T> buildFail(Integer code, String message) {
        return build(
                code,
                false,
                message
        );
    }

    /**
     * 失败返回（返回信息+数据）
     *
     * @param message 返回信息
     * @param data    数据
     * @param <T>     泛型
     * @return {@link Result<T>}
     */
    public static <T> Result<T> buildFail(String message, T data) {
        return build(
                HttpStatusEnum.INTERNAL_SERVER_ERROR.getCode(),
                false,
                message,
                data
        );
    }


    /**
     * 失败返回（状态码+返回信息+数据）
     *
     * @param code    状态码
     * @param message 返回消息
     * @param data    数据
     * @param <T>     泛型
     * @return {@link Result<T>}
     */
    public static <T> Result<T> buildFail(Integer code, String message, T data) {
        return build(
                code,
                false,
                message,
                data
        );
    }

    /**
     * 失败返回（数据）
     *
     * @param data 数据
     * @param <T>  泛型
     * @return {@link Result<T>}
     */
    public static <T> Result<T> buildFail(T data) {
        return build(
                HttpStatusEnum.INTERNAL_SERVER_ERROR.getCode(),
                false,
                HttpStatusEnum.INTERNAL_SERVER_ERROR.getMessage(),
                data
        );
    }

    /**
     * 失败返回（返回信息）
     *
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link Result<T>}
     */
    public static <T> Result<T> buildFail(String message) {
        return build(
                HttpStatusEnum.INTERNAL_SERVER_ERROR.getCode(),
                false,
                message,
                null
        );
    }

}
