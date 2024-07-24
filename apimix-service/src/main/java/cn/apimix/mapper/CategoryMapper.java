package cn.apimix.mapper;

import cn.apimix.model.entity.Category;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 *  映射层。
 *
 * @author Hor
 * @since 2024-05-31
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}
