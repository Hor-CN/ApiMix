package cn.apimix.model.vo.api;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author: Hor
 * @Date: 2024/6/24 下午7:59
 * @Version: 1.0
 */
@Data
@Builder
public class ApiRelationVo {

    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 接口 id
     */
    private Long apiId;

    /**
     * 总调用次数(总额度，包括所有的套餐)
     */
    private Long totalQuota;

    /**
     * 已调用次数(已用额度)
     */
    private Long usedQuota;

    /**
     * 1-正常，0-禁用
     */
    private Boolean status;

    /**
     * 创建时间（申请日期）
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    // 接口信息

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口 logo 图标
     */
    private String logo;


}
