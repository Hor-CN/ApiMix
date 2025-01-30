package cn.apimix.model.vo.console.analyse;

import cn.apimix.model.vo.console.ChartDataVo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/11/26 11:26
 * @Version: 1.0
 */
@Data
@Builder
public class ActiveVo {

    /**
     * 12个月的活跃接口统计
     */
    private List<ChartDataVo> dataList;

    /**
     * 总活跃数
     */
    private Long total;


    /**
     * 当天的活跃数
     */
    private Long today;

    /**
     * 昨日活跃数
     */
    private Long yesterday;


}
