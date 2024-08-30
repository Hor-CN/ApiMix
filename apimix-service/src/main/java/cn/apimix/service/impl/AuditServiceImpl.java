package cn.apimix.service.impl;

import cn.apimix.mapper.AuditMapper;
import cn.apimix.model.entity.Audit;
import cn.apimix.model.entity.AuditRecord;
import cn.apimix.model.entity.table.AuditRecordTableDef;
import cn.apimix.model.entity.table.AuditTableDef;
import cn.apimix.service.IAuditService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/27 22:58
 * @Version: 1.0
 */
@Service
public class AuditServiceImpl extends ServiceImpl<AuditMapper, Audit> implements IAuditService {


    @Resource
    private AuditDetailServiceImpl auditDetailService;

    /**
     * 根据类型和状态获取审核结果
     *
     * @param type   审核类型
     * @param status 审核状态
     */
    @Override
    public List<Audit> selectAuditByTypeAndStatus(Integer type, Integer status) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(AuditTableDef.AUDIT.TYPE.eq(type))
                .and(AuditTableDef.AUDIT.STATUS.eq(status));
        return list(queryWrapper);
    }


    public Audit selectAuditStatus(Long flowNo, Integer type) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(AuditTableDef.AUDIT.TYPE.eq(type))
                .and(AuditTableDef.AUDIT.FLOW_NO.eq(flowNo));
        return getOne(queryWrapper);
    }


    /**
     * 获取审核明细
     */
    public List<AuditRecord> selectAuditRecordByFlowNo(Long flowNo) {
        Long id = getOne(query().where(AuditTableDef.AUDIT.FLOW_NO.eq(flowNo))).getId();
        return auditDetailService.getMapper().selectListByQuery(query()
                .where(AuditRecordTableDef.AUDIT_RECORD.AUDIT_ID.eq(id))
                .orderBy(AuditRecordTableDef.AUDIT_RECORD.CREATE_TIME.desc())
        );
    }


    /**
     * 添加审核
     *
     * @param audit 审核
     */
    @Transactional
    @Override
    public Boolean insertAudit(Audit audit) {
        // 新增审核
        boolean save = save(audit);
        // 审核明细
        auditDetailService.save(AuditRecord.builder()
                .auditId(audit.getId())
                .status(1)
                .remark("用户提交")
                .build());
        return save;
    }


    /**
     * 修改审核
     */
    @Transactional
    public Boolean updateAudit(Long flowNo, Long userId, Integer status, String remark) {
        Audit one = getOne(query().where(
                AuditTableDef.AUDIT.FLOW_NO.eq(flowNo))
        );
        one.setStatus(status);
        boolean b = updateById(one);
        // 新增审核明细
        auditDetailService.save(AuditRecord.builder()
                .auditId(one.getId())
                .status(status)
                .userId(userId)
                .remark(remark)
                .build());
        return b;
    }

}
