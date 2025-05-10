package cn.apimix.audit.service.impl;


import cn.apimix.audit.mapper.AuditDetailMapper;
import cn.apimix.audit.service.AuditDetailService;
import cn.apimix.audit.model.entity.AuditRecord;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Author: Hor
 * @Date: 2024/5/27 23:00
 * @Version: 1.0
 */
@Service
public class AuditDetailServiceImpl extends ServiceImpl<AuditDetailMapper, AuditRecord> implements AuditDetailService {


}
