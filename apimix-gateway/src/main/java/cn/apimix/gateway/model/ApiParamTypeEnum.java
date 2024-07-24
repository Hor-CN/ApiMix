package cn.apimix.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Hor
 * @Date: 2024/6/14 21:31
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum ApiParamTypeEnum {

    STRING("string"),
    INT("int"),
    BOOLEAN("boolean"),
    NUMBER("number"),
    ARRAY("array"),
    FILE("file");

    private final String type;

}