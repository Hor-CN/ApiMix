package cn.apimix.common.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Hor
 * @Date: 2024/7/25 下午7:13
 * @Version: 1.0
 */
@Data
@Builder
public class InterfaceInfo implements Serializable {
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
     * 请求类型：GET、PUT、POST
     */
    private String method;

    /**
     * 状态：0-下线 1 上线
     */
    private Boolean status;

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
