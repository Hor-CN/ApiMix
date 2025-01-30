package cn.apimix.mapper;

import cn.apimix.model.entity.AuditRecord;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: Hor
 * @Date: 2024/5/27 22:55
 * @Version: 1.0
 */
@Mapper
public interface AuditDetailMapper extends BaseMapper<AuditRecord> {
}
