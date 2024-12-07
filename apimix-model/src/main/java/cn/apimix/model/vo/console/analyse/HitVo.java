package cn.apimix.model.vo.console.analyse;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: Hor
 * @Date: 2024/11/26 14:19
 * @Version: 1.0
 */
@Data
@Builder
public class HitVo {

    // 总次数
    private Long totalNumber;

    // 成功次数
    private Long successNumber;

    // 失败次数
    private Long failedNumber;


    
}
