package cn.apimix.model.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Hor
 * @Date: 2024/5/20 16:07
 * @Version: 1.0
 * @Description: API参数属于那部分
 * response|request
 */
@Getter
@AllArgsConstructor
public enum ApiParamPartEnum {


    REQUEST("request"),
    RESPONSE("response");

    @EnumValue
    private final String part;

}
