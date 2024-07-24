package cn.apimix.common.enums;

/**
 * 响应码接口，自定义响应码，实现此接口
 * @author hor
 * @date 2023/10/22 10:21
 */
public interface HttpStatus {

    Integer getCode();

    Boolean getStatus();

    String getMessage();

}