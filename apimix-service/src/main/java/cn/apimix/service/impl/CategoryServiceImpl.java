package cn.apimix.service.impl;


import cn.apimix.mapper.CategoryMapper;
import cn.apimix.model.entity.Category;
import cn.apimix.model.entity.table.CategoryTableDef;
import cn.apimix.service.ICategoryService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务层实现。
 *
 * @author Hor
 * @since 2024-05-31
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    /**
     * 获取分类信息
     *
     * @param id 分类ID
     */
    @Override
    public Category selectCategoryById(Long id) {
        return getById(id);
    }

    /**
     * 获取分类列表
     */
    @Override
    public List<Category> selectCategoryByList() {
        return list(QueryWrapper.create().orderBy(CategoryTableDef.CATEGORY.ORDER.asc()));
    }

    /**
     * 新增分类
     *
     * @param category 分类
     */
    @Override
    public Boolean insertCategory(Category category) {
        return save(category);
    }

    /**
     * 修改分类
     *
     * @param category 分类信息

     */
    @Override
    public Boolean updateCategory(Category category) {
        return updateById(category);
    }



}
