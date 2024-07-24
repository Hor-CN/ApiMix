package cn.apimix.invoke.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/6/14 21:32
 * @Version: 1.0
 */
@Data
@Builder
public class RequestParams {

    /**
     * 请求地址
     */
    private  String url;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 请求头参数
     */
    private List<ApiParamField> header;

    /**
     * 请求查询参数
     */
    private List<ApiParamField> query;

    /**
     * 请求体参数
     */
    private List<ApiParamField> body;

}
