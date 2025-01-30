package cn.apimix.model.vo.console.analyse;

import cn.apimix.model.vo.console.ChartDataVo;
import cn.apimix.model.vo.console.home.Xy;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/11/25 15:08
 * @Version: 1.0
 */
@Data
@Builder
public class PvVo {


    /**
     * 12个月的请求统计
     */
    private List<ChartDataVo> dataList;

    /**
     * 总请求次数
     */
    private Long total;

    /**
     * 当天的请求次数
     */
    private Long today;

    /**
     * 昨日请求次数
     */
    private Long yesterday;

}
