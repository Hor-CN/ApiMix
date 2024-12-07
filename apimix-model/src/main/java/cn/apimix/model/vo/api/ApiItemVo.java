package cn.apimix.model.vo.api;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author: Hor
 * @Date: 2024/12/4 16:24
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
     * 分类ID
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
