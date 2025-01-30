package cn.apimix.model.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: Hor
 * @Date: 2024/8/26 下午12:36
 * @Version: 1.0
 */
@Data
@Builder
public class SearchCountInfoVo {

    @Builder.Default
    private Long apiCount = 0L;

    @Builder.Default
    private Long privateCount = 0L;

    @Builder.Default
    private Long dataCount = 0L;

    @Builder.Default
    private Long userCount = 0L;

}
