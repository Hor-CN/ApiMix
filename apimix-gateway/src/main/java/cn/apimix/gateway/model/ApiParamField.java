package cn.apimix.gateway.model;

/**
 * @Author: Hor
 * @Date: 2024/6/14 21:30
 * @Version: 1.0
 */

import lombok.Builder;
import lombok.Data;

/**
 * @Author: Hor
 * @Date: 2024/5/20 15:50
 * @Version: 1.0
 * @Description: 请求参数字段
 */
@Builder
@Data
public class ApiParamField {
    /**
     * 参数名
     */
    private String name;

    /**
     * 参数的类型
     */
    private ApiParamTypeEnum type;

    /**
     * 参数值
     */
    private Object value;


}
