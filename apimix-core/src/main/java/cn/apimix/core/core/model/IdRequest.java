package cn.apimix.core.core.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * ID 请求
 * @Author: Hor
 * @Date: 2024/5/20 16:19
 * @Version: 1.0
 */
@Data
public class IdRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    @NotNull(message = "id 不能为空")
    private Long id;
}
