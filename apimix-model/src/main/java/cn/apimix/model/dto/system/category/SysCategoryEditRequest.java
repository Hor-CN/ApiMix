package cn.apimix.model.dto.system.category;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Hor
 * @Date: 2024/5/31 18:04
 * @Version: 1.0
 */
@Data
public class SysCategoryEditRequest implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 分类ID
     */
    private Long id;

    /**
     * 分类名称
     */
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
