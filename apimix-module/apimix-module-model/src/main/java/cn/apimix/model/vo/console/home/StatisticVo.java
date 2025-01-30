package cn.apimix.model.vo.console.home;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: Hor
 * @Date: 2024/11/23 21:01
 * @Version: 1.0
 */
@Data
@Builder
public class StatisticVo {

    // 贡献统计
    private Long contribute;

    //  已申请API
    private Long applied;

    // 余额
    private Long amount;

}
