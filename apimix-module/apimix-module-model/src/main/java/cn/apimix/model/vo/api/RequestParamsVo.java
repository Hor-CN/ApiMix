package cn.apimix.model.vo.api;

import cn.apimix.model.entity.ApiParam;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 请求参数
 *
 * @Author: Hor
 * @Date: 2024/5/25 17:28
 * @Version: 1.0
 */
@Data
@Builder
public class RequestParamsVo {

    /**
     * 请求头参数
     */
    private List<ApiParam> header;

    /**
     * 请求体参数
     */
    private List<ApiParam> body;

    /**
     * 请求查询参数
     */
    private List<ApiParam> query;
}
