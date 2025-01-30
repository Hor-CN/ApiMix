package cn.apimix.service;

import cn.apimix.model.entity.CategoryApi;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * 服务层。
 *
 * @author Hor
 * @since 2024-05-31
 */
public interface CategoryApiService extends IService<CategoryApi> {


    /**
     * 根据接口ID获取分类列表ID
     */
    List<Long> selectCategoryByApiId(Long apiId);


}
