package cn.apimix.model.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Hor
 * @Date: 2024/5/20 16:05
 * @Version: 1.0
 * @Description: Api参数归属
 *  query|body|header
 */
@Getter
@AllArgsConstructor
public enum ApiParamInEnum {

    QUERY("query"),
    BODY("body"),
    HEADER("header");

    @EnumValue
    private final String in;

}
