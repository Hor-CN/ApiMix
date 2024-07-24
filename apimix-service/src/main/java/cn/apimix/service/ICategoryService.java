package cn.apimix.service;

import cn.apimix.model.entity.Category;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * 服务层。
 *
 * @author Hor
 * @since 2024-05-31
 */
public interface ICategoryService extends IService<Category> {

    /**
     * 获取分类信息
     */
    Category selectCategoryById(Long id);

    /**
     * 获取分类列表
     */
    List<Category> selectCategoryByList();


    /**
     * 新增分类
     */
    Boolean insertCategory(Category category);

    /**
     * 修改分类
     */
    Boolean updateCategory(Category category);

}
