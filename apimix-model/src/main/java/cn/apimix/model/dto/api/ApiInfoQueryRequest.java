package cn.apimix.model.dto.api;

import cn.apimix.core.core.model.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author: Hor
 * @Date: 2024/5/25 16:37
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApiInfoQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 接口名称
     */
    private String name;

}
