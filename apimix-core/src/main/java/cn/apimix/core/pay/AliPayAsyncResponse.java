package cn.apimix.core.pay;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Hor
 * @Date: 2025/1/20 23:47
 * @Version: 1.0
 */
@Data
public class AliPayAsyncResponse implements Serializable {
    private static final long serialVersionUID = 1061553753198699097L;

    /**
     * 通知时间
     * -	通知时间。通知的发送时间。格式为 yyyy-MM-dd HH:mm:ss。
     */
    private String notifyTime;

    /**
     * 通知类型
     * 枚举值：trade_status_sync。
     */
    private String notifyType;

    /**
     * 签名类型。
     * 商家生成签名字符串所使用的签名算法类型，目前支持 RSA2 和 RSA，推荐使用 RSA2（如果开发者手动验签，不使用 SDK 验签，可以不传此参数）
     */
    private String signType;

    /**
     * 签名。可查看异步返回结果的验签（如果开发者手动验签，不使用 SDK 验签，可以不传此参数）。
     */
    private String sign;

    /**
     * 通知校验ID
     */
    private String notifyId;

    /**
     * 卖家id
     */
    private String sellerId;

    /**
     * 买方id
     */
    private String buyerId;

    /**
     * 编码格式
     */
    private String charset;

    /**
     * 接口版本
     */
    private String version;

    /**
     * 授权方的app_id
     */
    private String authAppId;

    /**
     * 支付宝交易号
     */
    private String tradeNo;

    /**
     * APP_ID
     */
    private String appId;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 交易状态
     */
    private String tradeStatus;

    /**
     * 订单金额
     */
    private String totalAmount;

    /**
     * 实收金额
     */
    private String receiptAmount;

    /**
     * 付款金额
     */
    private String buyerPayAmount;

    /**
     * 订单标题
     */
    private String subject;

    /**
     * 商品描述
     */
    private String body;

    /**
     * 交易创建时间
     */
    private String gmtCreate;
}