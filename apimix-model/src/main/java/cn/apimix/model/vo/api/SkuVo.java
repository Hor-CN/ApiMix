package cn.apimix.model.vo.api;

import cn.apimix.model.entity.Package;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/6/17 下午6:51
 * @Version: 1.0
 */
@Builder
@Data
public class SkuVo {

    private Integer id;

    private String name;

    private List<Package> list;
}
