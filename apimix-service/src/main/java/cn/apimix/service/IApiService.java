package cn.apimix.service;

import cn.apimix.model.dto.api.ApiAddRequest;
import cn.apimix.model.dto.api.ApiEditRequest;
import cn.apimix.model.dto.api.ApiInfoQueryRequest;
import cn.apimix.model.entity.ApiInfo;
import cn.apimix.model.vo.api.ApiItemVo;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

/**
 * @Author: Hor
 * @Date: 2024/5/25 16:14
 * @Version: 1.0
 */
public interface IApiService extends IService<ApiInfo> {

    /**
     * 上传接口
     *
     * @param addRequest 添加接口信息
     * @param userId     用户Id
     */
    Boolean insertApi(ApiAddRequest addRequest, Long userId);

    /**
     * 根据接口ID获取接口信息
     *
     * @param apiId 接口ID
     * @return 结果
     */
    ApiItemVo selectApiInfoByApiId(Long apiId);

    /**
     * 根据接口ID删除接口
     *
     * @param apiId 接口ID
     * @return 结果
     */
    Boolean deleteApiInfoByApiId(Long apiId);

    /**
     * 更新接口信息
     *
     * @param editRequest 接口信息
     * @return 结果
     */
    Boolean updateApiInfo(ApiEditRequest editRequest);

    /**
     * 分页获取API接口列表
     */
    Page<ApiInfo> selectApiInfoByPage(ApiInfoQueryRequest queryRequest);

    /**
     * 分页获取API接口列表
     */
    Page<ApiInfo> selectApiInfoByCategory(ApiInfoQueryRequest queryRequest,Long categoryId);


    /**
     * 分页获取API接口列表当前开发者
     */
    Page<ApiInfo> selectDevApiInfoByPage(ApiInfoQueryRequest queryRequest, Long userId);


    /**
     * 分页获取待审核的接口列表
     */
    Page<ApiInfo> selectAuditApiInfoByPage(ApiInfoQueryRequest queryRequest);


    /**
     * 统计该开发者上传已通过审核的接口
     */
    Long selectApiBycCount(Long userId);

}
