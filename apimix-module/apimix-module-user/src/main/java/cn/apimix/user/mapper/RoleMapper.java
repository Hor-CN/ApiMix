package cn.apimix.user.mapper;


import cn.apimix.user.model.entity.Role;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 *  映射层。
 *
 * @author Hor
 * @since 2023-10-30
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

}
