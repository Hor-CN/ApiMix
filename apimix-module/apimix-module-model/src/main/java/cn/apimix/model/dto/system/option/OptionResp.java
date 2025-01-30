package cn.apimix.model.dto.system.option;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.io.Serializable;

/**
 * 参数信息
 *
 * @Author: Hor
 * @Date: 2024/10/22 11:53
 * @Version: 1.0
 */
@Data
public class OptionResp implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 键
     */
    private String code;

    /**
     * 值
     */
    private String value;

    /**
     * 默认值
     */
    @JsonIgnore
    private String defaultValue;

    /**
     * 描述
     */
    private String description;

    public String getValue() {
        return StrUtil.nullToDefault(value, defaultValue);
    }
}