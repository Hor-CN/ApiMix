package cn.apimix.model.dto.system.option;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: Hor
 * @Date: 2024/10/22 12:24
 * @Version: 1.0
 */
@Data
public class OptionReq implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @NotNull(message = "ID不能为空")
    private Long id;

    /**
     * 键
     */
    @NotBlank(message = "键不能为空")
    @Length(max = 100, message = "键长度不能超过 {max} 个字符")
    private String code;

    /**
     * 值
     */
    private String value;
}