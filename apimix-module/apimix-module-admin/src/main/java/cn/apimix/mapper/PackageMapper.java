package cn.apimix.mapper;

import cn.apimix.model.entity.Package;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 映射层。
 *
 * @author Hor
 * @since 2024-06-17
 */
@Mapper
public interface PackageMapper extends BaseMapper<Package> {

}
