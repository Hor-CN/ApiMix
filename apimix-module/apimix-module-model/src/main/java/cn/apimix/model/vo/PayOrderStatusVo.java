package cn.apimix.model.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: Hor
 * @Date: 2025/1/25 14:22
 * @Version: 1.0
 */
@Data
@Builder
public class PayOrderStatusVo {

    private Integer state;

    private String msg;

    private String status;

    private LocalDateTime expirationTime;

}
