package cn.apimix.model.dto.system.category;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Author: Hor
 * @Date: 2024/5/31 17:47
 * @Version: 1.0
 */
@Data
public class SysCategoryAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 分类名称
     */
    @NotBlank(message = "请输入分类名称")
    private String name;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer order;
}
