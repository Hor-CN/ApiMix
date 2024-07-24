package cn.apimix.model.dto.api.field;

import lombok.Data;

/**
 * @Author: Hor
 * @Date: 2024/5/20 16:11
 * @Version: 1.0
 * @Description: API 返回示例
 */
@Data
public class ApiExampleField {
    /**
     * 示例名称
     */
    private String name;

    /**
     * 状态码
     */
    private Long code;


    /**
     * 返回类型
     * application/json
     */
    private String type;

    /**
     * 示例内容
     */
    private String content;
}
