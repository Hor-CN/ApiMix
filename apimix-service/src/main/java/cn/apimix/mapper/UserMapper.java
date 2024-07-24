package cn.apimix.mapper;


import cn.apimix.model.entity.User;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 映射层。
 *
 * @author Hor
 * @since 2023-10-22
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
