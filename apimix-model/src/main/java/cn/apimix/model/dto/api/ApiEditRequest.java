package cn.apimix.model.dto.api;

import cn.apimix.model.entity.ApiExample;
import cn.apimix.model.vo.api.RequestParamsVo;
import cn.apimix.model.vo.api.ResponseParamsVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/6/3 14:21
 * @Version: 1.0
 */
@Data
public class ApiEditRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口地址
     */
    private String url;


    /**
     * 接口 logo 图标
     */
    private String logo;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 请求类型：GET、PUT、POST
     */
    private String method;

    /**
     * 状态：0-下线 1 上线
     */
    private Boolean status;

    /**
     * 分类ID
     */
    private Long category;

    /**
     * 接口介绍 MarkDown
     */
    private String content;

    /**
     * 请求参数
     */
    private RequestParamsVo request;

    /**
     * 响应参数
     */
    private ResponseParamsVo response;

    /**
     * 响应示例
     */
    private List<ApiExample> result;

    /**
     * 是否收费
     */
    private Boolean isPaid;

    /**
     * 用户ID（作者ID）
     */
    private Long userId;
}
