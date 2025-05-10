package cn.apimix.api.model.req;

import cn.apimix.core.model.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author: Hor
 * @Date: 2025/2/19 00:56
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApiQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 接口名称
     */
    private String name;

    private Boolean status;

}