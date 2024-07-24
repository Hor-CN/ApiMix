package cn.apimix.common.enums;


import lombok.Getter;

/**
 * @author Hor
 */

@Getter
public enum HttpStatusEnum implements HttpStatus {

    /**
     * 操作成功
     */
    SUCCESS(200, true, "操作成功"),
    /**
     * 对象创建成功
     */
    CREATED(201, true, "创建成功"),

    /**
     * 资源已被移除
     */
    MOVED_PERM(301, true, "资源已被移除"),

    FAIL(400, false, "请求失败"),
    PARAM_ERROR(400, false, "参数错误"),

    UNAUTHORIZED(401, false, "未认证（签名错误）"),

    FORBIDDEN(403, false, "访问受限，授权过期"),

    NOT_FOUND(404, false, "接口不存在"),

    METHOD_NOT_ALLOWED(405, false, "不支持该请求方法"),

    CONFLICT(409, false, "发生冲突"),

    UNSUPPORTED_MEDIA_TYPE(415, false, "不支持该请求信息格式"),

    INTERNAL_SERVER_ERROR(500, false, "服务器内部错误"),


    NULL_POINT(200002, false, "空指针异常");


    /**
     * 响应状态码
     */
    private final Integer code;

    /**
     * 状态
     */
    private final Boolean status;

    /**
     * 响应信息
     */
    private final String message;

    HttpStatusEnum(Integer code, Boolean status, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}