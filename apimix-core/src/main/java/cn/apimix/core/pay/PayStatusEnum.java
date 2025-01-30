package cn.apimix.core.pay;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Hor
 * @Date: 2025/1/23 10:46
 * @Version: 1.0
 */
public enum PayStatusEnum {

    /**
     * 支付成功
     */
    SUCCESS("支付成功", "SUCCESS"),

    /**
     * 支付失败
     */
    PAY_ERROR("支付失败", "PAYERROR"),
    /**
     * 用户付费中
     */
    USER_PAYING("用户支付中", "USER_PAYING"),
    /**
     * 已关闭
     */
    CLOSED("已关闭", "CLOSED"),

    /**
     * 未支付
     */
    NOTPAY("未支付", "NOTPAY"),
    /**
     * 转入退款
     */
    REFUND("转入退款", "REFUND"),
    /**
     * 退款中
     */
    PROCESSING("退款中", "PROCESSING"),
    /**
     * 撤销
     */
    REVOKED("已撤销（刷卡支付）", "REVOKED"),

    /**
     * 未知
     */
    UNKNOWN("未知状态", "UNKNOWN");


    private final String text;

    @Getter
    @EnumValue
    private final String status;

    PayStatusEnum(String text, String value) {
        this.text = text;
        this.status = value;
    }

    /**
     * 获取值
     * 得到值
     * 获取值列表
     *
     * @return {@link List}<{@link String}>
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.status).collect(Collectors.toList());
    }

    @JsonValue
    public String getText() {
        return text;
    }

}
