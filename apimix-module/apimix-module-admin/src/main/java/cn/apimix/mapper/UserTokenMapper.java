package cn.apimix.mapper;


import cn.apimix.model.entity.UserToken;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 映射层。
 *
 * @author Hor
 * @since 2023-11-11
 */
@Mapper
public interface UserTokenMapper extends BaseMapper<UserToken> {

}
