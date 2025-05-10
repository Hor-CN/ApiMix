package cn.apimix.api.mapper;

import cn.apimix.api.model.entity.ApiRelease;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 *  映射层。
 *
 * @author Hor
 * @since 2025-02-17
 */
@Mapper
public interface ApiReleaseMapper extends BaseMapper<ApiRelease> {

    <T> Page<ApiRelease> paginateWithRelations(Page<T> of, QueryWrapper queryWrapper, Class<Object> objectClass);
}
