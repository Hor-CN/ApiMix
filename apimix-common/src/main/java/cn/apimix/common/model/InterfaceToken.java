package cn.apimix.common.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: Hor
 * @Date: 2024/7/25 下午4:54
 * @Version: 1.0
 */
@Data
@Builder
public class InterfaceToken implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 用户 id
     */
    private Long userId;


    /**
     * 名称
     */
    private String name;

    /**
     * 状态
     */
    private Boolean status;


    /**
     * Token 值
     */
    private String tokenValue;

    /**
     * remark 备注
     */
    private String remark;

    /**
     * 过期时间
     */
    private LocalDateTime expired;

    /**
     * 创建时间（申请日期）
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
