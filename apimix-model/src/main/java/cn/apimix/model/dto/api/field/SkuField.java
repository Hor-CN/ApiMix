package cn.apimix.model.dto.api.field;

import cn.apimix.model.entity.Package;
import lombok.Data;

import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/6/19 下午10:21
 * @Version: 1.0
 */
@Data
public class SkuField {

    /**
     * 套餐类型ID
     */
    private Integer id;

    /**
     * 套餐类型名称
     */
    private String name;

    /**
     * 套餐列表
     */
    private List<Package> list;

}
