package cn.apimix.api.model.resp;

import cn.apimix.api.model.entity.ApiExample;
import cn.apimix.api.model.entity.Category;
import cn.apimix.audit.model.entity.Audit;
import cn.apimix.model.vo.sku.SkuVo;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2025/2/18 21:04
 * @Version: 1.0
 */
@Data
@Builder
public class ApiReleaseResp implements Serializable {
    private final static long serialVersionUID = 1L;

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
     * 状态：1-草稿，2-审核中，3-已发布，4-已下线
     */
    private Integer status;

    /**
     * 上线下线
     */
    private Boolean releaseStatus;

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
     * 版本号
     */
    private String version;

    /**
     * 版本介绍
     */
    private String versionDescription;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 审核
     */
    private Audit audit;

    /**
     * 分类列表
     */
    private List<Category> categories;

}
