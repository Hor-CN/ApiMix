package cn.apimix.model.vo.console.analyse;

import cn.apimix.model.vo.console.home.Xy;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/11/25 18:14
 * @Version: 1.0
 */
@Data
@Builder
public class IpVo {

    /**
     * 12个月的ip统计
     */
    private List<Xy> dataList;

    /**
     * 总IP数
     */
    private Long total;

    /**
     * 当天的IP数
     */
    private Long today;

    /**
     * 昨日IP数
     */
    private Long yesterday;

}
