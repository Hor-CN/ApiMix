package cn.apimix.model.vo.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @Author: Hor
 * @Date: 2024/8/12 上午11:26
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
public class MonitorItem {
    private String time;

    private String value;
}
