package cn.apimix.model.vo.api;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/8/10 下午8:12
 * @Version: 1.0
 */
@Data
@Builder
public class MonitorLine {

    /**
     * 总次数
     */
    private Long sum;

    /**
     * 元素列表
     */
    private List<MonitorItem> items;

}

