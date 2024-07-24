package cn.apimix.model.dto.api;

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
 * @Date: 2024/5/20 15:46
 * @Version: 1.0
 * @Description: 添加接口请求体
 */
@Data
public class ApiAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 接口名称
     */
    @NotBlank(message = "接口不能为空")
    private String name;

    /**
     * 接口Logo
     */
    private String logo;

    /**
     * 接口地址
     */
    @URL(message = "接口地址错误")
    private String url;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 请求类型：GET、PUT、POST
     */
    @NotBlank(message = "请求方式不能为空")
    @Pattern(regexp = "^(GET|POST|GET,POST)$", message = "请求方式只能是GET或POST或GET,POST")
    private String method;

    /**
     * 接口介绍 MarkDown
     */
    private String content;

    /**
     * 接口分类
     */
    @NotBlank(message = "接口分类不能为空")
    private Long category;


    /**
     * 是否收费
     */
    @NotBlank(message = "是否收费不能为空")
    private Boolean isPaid;


    /**
     * 收费代理
     */
    @NotBlank(message = "是否代理不能为空")
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
     * 添加套餐
     */
    private List<SkuField> packages;

    /**
     * 返回示例
     */
    private List<ApiExampleField> result;

}
