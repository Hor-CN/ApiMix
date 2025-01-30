package cn.apimix.core.model;

import cn.apimix.core.constant.CommonConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 分页请求
 * @Author: Hor
 * @Date: 2024/5/20 16:26
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest {

    /**
     * 每 1 页的数据量
     */
    @NotNull(message = "pageSize 每页显示记录不能为空")
    @Min(value = 1, message = "pageSize 每页显示记录必须大于等于1")
    private Long pageSize;

    /**
     * 当前页码，从 1 开始
     */
    @NotNull(message = "pageNumber 页码不能为空")
    @Min(value = 1,message = "pageNumber 页码必须大于等于1")
    private Long pageNumber;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;

}
