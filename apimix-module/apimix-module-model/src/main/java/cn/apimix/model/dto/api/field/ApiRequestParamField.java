package cn.apimix.model.dto.api.field;

import lombok.Data;

import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/20 15:48
 * @Version: 1.0
 * @Description: API 接口的请求参数
 */
@Data
public class ApiRequestParamField {

    /**
     * 请求头参数
     */
    private List<ApiParamField> header;

    /**
     * 请求体参数
     */
    private List<ApiParamField> body;

    /**
     * 请求查询参数
     */
    private List<ApiParamField> query;
}
