package cn.apimix.model.vo.api;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: Hor
 * @Date: 2024/8/9 下午8:41
 * @Version: 1.0
 */
@Data
@Builder
public class Quota {
    /**
     * 所有总调用次数(总额度，包括所有的套餐)
     */
    private Long allTotalQuota;

    /**
     * 所有已调用次数(已用额度)
     */
    private Long allUsedQuota;

    /**
     * 过期总调用次数(总额度，包括所有的套餐)
     */
    private Long pastTotalQuota;

    /**
     * 过期已调用次数(已用额度)
     */
    private Long pastUsedQuota;
}
