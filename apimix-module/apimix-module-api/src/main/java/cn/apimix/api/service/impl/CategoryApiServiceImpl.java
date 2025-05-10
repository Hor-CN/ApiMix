package cn.apimix.api.service.impl;

import cn.apimix.api.mapper.CategoryApiMapper;
import cn.apimix.api.model.entity.table.CategoryApiTableDef;
import cn.apimix.api.model.entity.CategoryApi;
import cn.apimix.api.service.CategoryApiService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  服务层实现。
 *
 * @author Hor
 * @since 2024-05-31
 */
@Service
public class CategoryApiServiceImpl extends ServiceImpl<CategoryApiMapper, CategoryApi> implements CategoryApiService {


    /**
     * 根据接口ID获取分类列表ID
     *
     * @param apiId 接口ID
     */
    @Override
    public List<Long> selectCategoryByApiId(Long apiId) {
        return listAs(queryChain().select(CategoryApiTableDef.CATEGORY_API.ID)
                .where(CategoryApiTableDef.CATEGORY_API.API_ID.eq(apiId)), Long.class);
    }
}
