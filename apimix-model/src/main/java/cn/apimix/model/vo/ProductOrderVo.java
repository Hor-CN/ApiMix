package cn.apimix.model.vo;

import cn.apimix.model.entity.Package;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author: Hor
 * @Date: 2024/8/29 下午8:25
 * @Version: 1.0
 */
@Data
@Builder
public class ProductOrderVo {
    /**
     * 订单类型（0：商品类型，1：充值类型）
     */
    private Integer type;

    /**
     * 用户ID
     */
    private Long userId;

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

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 套餐
     */
    private Package packageInfo;
}
