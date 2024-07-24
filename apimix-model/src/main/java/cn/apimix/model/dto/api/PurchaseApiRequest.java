package cn.apimix.model.dto.api;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Hor
 * @Date: 2024/6/24 下午4:50
 * @Version: 1.0
 */
@Data
public class PurchaseApiRequest implements Serializable {

    /**
     * 套餐ID
     */
    private Long packageId;

    /**
     * 套餐数量
     */
    private Integer count;
}
