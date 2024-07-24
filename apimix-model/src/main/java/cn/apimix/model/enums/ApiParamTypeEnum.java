package cn.apimix.model.enums;


import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Hor
 * @Date: 2024/5/20 15:52
 * @Version: 1.0
 * @Description: API接口参数的类型
 * string|int|boolean|number|array|file
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

    @EnumValue
    private final String type;

}