package cn.apimix.model.vo;

import cn.apimix.model.entity.Package;
import cn.apimix.core.pay.PayStatusEnum;
import cn.apimix.model.enums.PayTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: Hor
 * @Date: 2024/8/29 下午8:25
 * @Version: 1.0
 */
@Data
@Builder
public class ProductOrderVo {

    private Long orderNo;

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
    private PayTypeEnum payType;

    /**
     * 支付状态
     */
    private PayStatusEnum status;

    /**
     * 支付二维码地址
     */
    private String codeUrl;

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
    private LocalDateTime createTime;


    private LocalDateTime expirationTime;


    /**
     * 商品名称
     */
    private String productName;

    /**
     * 套餐
     */
    private Package packageInfo;
}
