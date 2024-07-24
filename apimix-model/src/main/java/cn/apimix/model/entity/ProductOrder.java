package cn.apimix.model.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单表
 *
 * @Author: Hor
 * @Date: 2024/6/24 下午1:59
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "product_order")
public class ProductOrder implements Serializable {

    /**
     * 主键ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 订单类型（0：商品类型，1：充值类型）
     */
    private Integer type;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 套餐ID
     */
    private Long packageId;

    /**
     * 金额
     */
    private Long price;

    /**
     * 付款方式
     */
    private Integer payType;

    /**
     * 数量
     */
    private Integer count;

    /**
     * 是否开发票
     */
    private Boolean invoice;

    /**
     * 创建时间
     */
    private Date createTime;

}
