package cn.apimix.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 第三方账号平台枚举
 *
 * @Author: Hor
 * @Date: 2024/12/15 22:35
 * @Version: 1.0
 */
@Getter
@RequiredArgsConstructor
public enum SocialSourceEnum {

    /**
     * 码云
     */
    GITEE("码云"),

    /**
     * GitHub
     */
    GITHUB("GitHub"),

    /**
     * 微信
     */
    WECHAT("微信"),;


    private final String description;

}
