package cn.apimix.model.dto.api.field;

import cn.apimix.model.enums.ApiParamInEnum;
import cn.apimix.model.enums.ApiParamPartEnum;
import cn.apimix.model.enums.ApiParamTypeEnum;
import lombok.Data;

/**
 * @Author: Hor
 * @Date: 2024/5/20 15:50
 * @Version: 1.0
 * @Description: 请求参数字段
 */
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
     * 是否必须
     */
    private Boolean isRequired;

    /**
     * 参数排序
     */
    private Integer order;

    /**
     * 示例值
     */
    private String example;

    /**
     * 备注
     */
    private String explain;


    /**
     * 参数属于的部分
     * query|body|header
     */
    private ApiParamInEnum in;

    /**
     * API参数属于那部分
     * response|request
     */
    private ApiParamPartEnum part;
}
