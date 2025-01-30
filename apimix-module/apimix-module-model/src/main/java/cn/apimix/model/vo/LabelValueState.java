package cn.apimix.model.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: Hor
 * @Date: 2025/1/2 10:08
 * @Version: 1.0
 */
@Builder
@Data
public class LabelValueState {

    private String label;

    private Long value;

}
