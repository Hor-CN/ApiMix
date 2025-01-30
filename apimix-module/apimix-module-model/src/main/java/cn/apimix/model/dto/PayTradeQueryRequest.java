package cn.apimix.model.dto;

import lombok.Data;

/**
 * @Author: Hor
 * @Date: 2025/1/25 13:47
 * @Version: 1.0
 */

@Data
public class PayTradeQueryRequest {

    private Long orderId;

    private String orderNo;

}
