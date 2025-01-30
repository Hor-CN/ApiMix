package cn.apimix.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;

/**
 * @Author: Hor
 * @Date: 2025/1/22 15:41
 * @Version: 1.0
 */

public enum PayTypeEnum {
    Alipay("AliPay"),
    PointPay("PointPay");


    @EnumValue
    private String payType;

    PayTypeEnum(String payType) {
        this.payType = payType;
    }

    @JsonValue
    public String getPayType() {
        return payType;
    }

}
