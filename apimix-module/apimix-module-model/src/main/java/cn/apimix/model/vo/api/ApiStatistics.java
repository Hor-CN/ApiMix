package cn.apimix.model.vo.api;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: Hor
 * @Date: 2024/8/10 下午2:34
 * @Version: 1.0
 */
@Data
@Builder
public class ApiStatistics {

    // 总次数
    private Long totalNumber;

    // 成功次数
    private Long successNumber;

    // 失败次数
    private Long failedNumber;
}
