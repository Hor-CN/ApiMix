package cn.apimix.model.dto.api.field;

import lombok.Data;

import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/20 16:10
 * @Version: 1.0
 * @Description: API 响应参数
 */
@Data
public class ApiResponseParamField {
    /**
     * 响应头参数
     */
    private List<ApiParamField> header;

    /**
     * 响应体参数
     */
    private List<ApiParamField> body;

    /**
     * 响应体的类型
     */
    private String type;
}
