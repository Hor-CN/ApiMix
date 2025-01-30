package cn.apimix.model.vo.api;

import cn.apimix.model.entity.ApiParam;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 响应参数
 *
 * @Author: Hor
 * @Date: 2024/5/25 17:28
 * @Version: 1.0
 */
@Data
@Builder
public class ResponseParamsVo {

    /**
     * 响应头参数
     */
    private List<ApiParam> header;

    /**
     * 响应体参数
     */
    private List<ApiParam> body;

    /**
     * 响应体的类型
     */
    private String type;

}
