package cn.apimix.api.model.req;

import cn.apimix.api.model.entity.ApiExample;
import cn.apimix.model.dto.api.field.ApiExampleField;
import cn.apimix.model.dto.api.field.ApiRequestParamField;
import cn.apimix.model.dto.api.field.ApiResponseParamField;
import cn.apimix.model.dto.api.field.SkuField;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2025/2/19 23:30
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
    @NotBlank(message = "接口不能为空")
    private String name;

    /**
     * 接口地址
     */
    @URL(message = "接口地址错误")
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
    @NotBlank(message = "请求方式不能为空")
    @Pattern(regexp = "^(GET|POST|PUT|DELETE|GET,POST)$", message = "请求方式只能是GET或POST或GET,POST")
    private String method;

    /**
     * 接口介绍 MarkDown
     */
    private String content;

    /**
     * 接口分类
     */
    private Long category;

    /**
     * 是否收费
     */
    private Boolean isPaid;

    /**
     * 是否代理
     */
    private Boolean proxy;

    /**
     * 请求参数
     */
    private ApiRequestParamField request;

    /**
     * 响应参数
     */
    private ApiResponseParamField response;

    /**
     * 返回示例
     */
    private List<ApiExampleField> result;

    /**
     * 添加套餐
     */
    private List<SkuField> packages;

    /**
     * 用户ID（作者ID）
     */
    private Long userId;

    /**
     * 接口版本
     */
    @NotBlank(message = "接口版本不能为空")
    private String version;

    /**
     * 版本介绍
     */
    private String versionDescription;
}
