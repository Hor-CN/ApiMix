package cn.apimix.api.service.impl;


import cn.apimix.api.mapper.ApiVersionMapper;
import cn.apimix.api.model.entity.ApiVersion;
import cn.apimix.api.model.entity.table.ApiVersionTableDef;
import cn.apimix.api.model.req.ApiQueryRequest;
import cn.apimix.api.service.ApiVersionService;
import cn.apimix.audit.model.entity.table.AuditTableDef;
import cn.apimix.audit.service.AuditService;
import cn.apimix.core.model.PageRequest;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.StringUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 *  服务层实现。
 *
 * @author Hor
 * @since 2025-02-18
 */
@Service
public class ApiVersionServiceImpl extends ServiceImpl<ApiVersionMapper, ApiVersion> implements ApiVersionService {

    @Resource
    private AuditService auditService;

    /**
     * 获取审核的接口
     * 状态：1-草稿，2-审核中，3-已发布，4-已下线
     *
     * @param queryRequest 搜索条件
     * @param status       审核状态
     * @return 结果集
     */
    @Override
    public Page<ApiVersion> getApiVersionByAudit(ApiQueryRequest queryRequest, Integer status) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(ApiVersionTableDef.API_VERSION.ALL_COLUMNS)
                .leftJoin(AuditTableDef.AUDIT)
                .on(ApiVersionTableDef.API_VERSION.ID.eq(AuditTableDef.AUDIT.FLOW_NO))
                .where(AuditTableDef.AUDIT.STATUS.eq(status))
                .and(ApiVersionTableDef.API_VERSION.NAME.like(queryRequest.getName(), StringUtil::isNotBlank));
        return page(Page.of(queryRequest.getPageNumber(), queryRequest.getPageSize()),
                queryWrapper);
    }

    /**
     * 获取审核的接口版本列表
     *
     * @param queryRequest 搜索条件
     * @param status       状态
     * @return 结果集
     */
    @Override
    public Page<ApiVersion> getApiVersionAuditByUserId(PageRequest queryRequest,Long apiId,String name ,Integer status, Long userId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(ApiVersionTableDef.API_VERSION.ALL_COLUMNS)
                .leftJoin(AuditTableDef.AUDIT)
                .on(ApiVersionTableDef.API_VERSION.ID.eq(AuditTableDef.AUDIT.FLOW_NO))
                .where(AuditTableDef.AUDIT.STATUS.eq(status))
                .and(ApiVersionTableDef.API_VERSION.NAME.like(name, StringUtil::isNotBlank))
                .and(ApiVersionTableDef.API_VERSION.API_ID.eq(apiId))
                .and(ApiVersionTableDef.API_VERSION.USER_ID.eq(userId))
                .orderBy(ApiVersionTableDef.API_VERSION.VERSION.desc(),ApiVersionTableDef.API_VERSION.CREATE_TIME.desc());

        return mapper.paginateWithRelations(Page.of(queryRequest.getPageNumber(), queryRequest.getPageSize()),
                queryWrapper);
    }

    /**
     * 提交审核
     *
     * @param id 版本ID
     * @return Boolean
     */
    @Transactional
    @Override
    public Boolean addApiVersionAudit(Long id) {
        updateById(ApiVersion.builder()
                .id(id)
                .status(2)
                .build());
        return auditService.insertApiAudit(id);
    }


}
