package cn.apimix.model.vo.api;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: Hor
 * @Date: 2024/12/21 21:23
 * @Version: 1.0
 */
@Data
@Builder
public class AllocationTokenVO {

    /**
     * 主键
     */
    private Long id;

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
     * 分配额度
     */
    private Long totalQuota;

    /**
     * 已用额度
     */
    private Long usedQuota;

    /**
     * 是否不受限制的 Token
     */
    private Boolean isUnlimited;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
