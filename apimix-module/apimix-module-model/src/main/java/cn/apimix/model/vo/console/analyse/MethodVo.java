package cn.apimix.model.vo.console.analyse;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: Hor
 * @Date: 2024/11/26 15:00
 * @Version: 1.0
 */
@Data
@Builder
public class MethodVo {
    private Long get;
    private Long post;
    private Long put;
    private Long delete;
    private Long other;
}
