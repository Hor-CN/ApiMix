package cn.apimix.api.service;

import cn.apimix.api.model.entity.ApiVersion;
import cn.apimix.api.model.req.ApiQueryRequest;
import cn.apimix.core.model.PageRequest;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
/**
 *  服务层。
 *
 * @author Hor
 * @since 2025-02-18
 */
public interface ApiVersionService extends IService<ApiVersion> {


    /**
     * 获取审核的接口
     * 状态：1-草稿，2-审核中，3-已发布，4-已下线
     * @param status 审核状态
     * @return 结果集
     */
    Page<ApiVersion> getApiVersionByAudit(ApiQueryRequest queryRequest,Integer status);

    /**
     * 获取审核的接口版本列表
     *
     * @param queryRequest 搜索条件
     * @param status 状态
     * @return 结果集
     */
    Page<ApiVersion> getApiVersionAuditByUserId(PageRequest queryRequest,Long apiId, String name, Integer status,Long userId);


    /**
     *  提交审核
     * @param id 版本ID
     * @return Boolean
     */
    Boolean addApiVersionAudit(Long id);
}
