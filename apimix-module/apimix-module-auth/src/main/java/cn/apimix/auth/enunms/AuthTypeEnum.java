package cn.apimix.auth.enunms;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 认证类型枚举
 *
 * @Author: Hor
 * @Date: 2025/1/29 11:14
 * @Version: 1.0
 */
@Getter
@RequiredArgsConstructor
public enum AuthTypeEnum {

    /**
     * 账号
     */
    ACCOUNT("ACCOUNT", "账号"),

    /**
     * 邮箱
     */
    EMAIL("EMAIL", "邮箱"),

    /**
     * 手机号
     */
    WX("WX", "微信公众号"),

    /**
     * 第三方账号
     */
    SOCIAL("SOCIAL", "第三方账号");

    private final String value;
    private final String description;

}
