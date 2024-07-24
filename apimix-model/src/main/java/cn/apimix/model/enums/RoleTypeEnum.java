package cn.apimix.model.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Hor
 * @Date: 2024/5/20 23:36
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum RoleTypeEnum {

    /**
     * 内置
     */
    INTERNAL(1),
    /**
     * 自定义
     */
    EXTERNAL(2);

    @EnumValue
    private final int type;
}
