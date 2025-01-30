package cn.apimix.model.vo;

import lombok.Data;

/**
 * @Author: Hor
 * @Date: 2025/1/25 22:29
 * @Version: 1.0
 */
@Data
public class LabelValueResp<T> {

    private String label;
    private T value;
    private String extra;

    public LabelValueResp(String label, T value, String extra) {
        this.label = label;
        this.value = value;
        this.extra = extra;
    }

    public LabelValueResp(String label, T value) {
        this.label = label;
        this.value = value;
    }


}
