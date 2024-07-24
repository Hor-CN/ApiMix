package cn.apimix.core.exception;

import cn.apimix.common.enums.HttpStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Hor
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HorApiException extends RuntimeException implements HttpStatus {

    /**
     * 错误码
     */
    private Integer code;


    private  Boolean status;

    /**
     * 错误提示
     */
    private String message;


    public HorApiException(String message) {
        this.message = message;

    }

    public HorApiException(int code,String message) {
        this.code = code;
        this.message = message;
    }

    public HorApiException(HttpStatus httpStatus) {
        this.message = httpStatus.getMessage();
        this.code = httpStatus.getCode();
        this.status = httpStatus.getStatus();
    }

}