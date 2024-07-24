package cn.apimix.model.vo.api;

import cn.apimix.model.dto.api.field.SkuField;
import cn.apimix.model.entity.ApiExample;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/25 17:28
 * @Version: 1.0
 */
@Data
@Builder
public class ApiItemVo {

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
     * 套餐
     */
    private List<SkuVo> packages;

    /**
     * 是否收费
     */
    private Boolean isPaid;

    /**
     * 是否代理
     */
    private Boolean proxy;

    /**
     * 用户ID（作者ID）
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}