package cn.apimix.model.dto.api;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Hor
 * @Date: 2024/8/12 下午2:07
 * @Version: 1.0
 */
@Data
public class MonitorLineRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long startTime;
    private Long endTime;

}
