package cn.apimix.audit.mapper;

import cn.apimix.audit.model.entity.Audit;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: Hor
 * @Date: 2024/5/27 22:54
 * @Version: 1.0
 */
@Mapper
public interface AuditMapper extends BaseMapper<Audit> {
}
