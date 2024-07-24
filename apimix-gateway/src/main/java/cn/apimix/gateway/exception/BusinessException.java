package cn.apimix.gateway.exception;

/**
 * @Author: Hor
 * @Date: 2024/6/15 16:58
 * @Version: 1.0
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -4593480471566176059L;
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}