package cn.apimix.model.vo.api;

import lombok.*;

import java.util.Date;

/**
 * @Author: Hor
 * @Date: 2024/6/24 下午7:59
 * @Version: 1.0
 */
@Setter
@Getter
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


    private Quota quota;

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

    public ApiRelationVo(Long id, Long userId, Long apiId, Boolean status, Date createTime, Date updateTime, String name, String logo) {
        this.id = id;
        this.userId = userId;
        this.apiId = apiId;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.name = name;
        this.logo = logo;
    }


}

