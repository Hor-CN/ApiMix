package cn.apimix.api.service;


import cn.apimix.api.model.entity.ApiRelease;
import cn.apimix.api.model.req.ApiAddRequest;
import cn.apimix.api.model.req.ApiEditRequest;
import cn.apimix.api.model.req.ApiQueryRequest;
import cn.apimix.api.model.resp.ApiReleaseResp;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

/**
 *  服务层。
 *
 * @author Hor
 * @since 2025-02-17
 */
public interface ApiReleaseService extends IService<ApiRelease> {

    /**
     * 发布接口
     * @return boolean
     */
    Boolean publishInterFace(ApiAddRequest addRequest, Long userId);

    /**
     * 更新接口
     * @param editRequest 更新信息
     * @param userId 用户ID
     * @return Boolean
     */
    Boolean updateInterface(ApiEditRequest editRequest, Long userId);

    /**
     * 根据接口ID获取接口信息
     */
    ApiReleaseResp getInterfaceById(Long id);

    /**
     * 根据接口ID删除接口
     *
     * @param apiId 接口ID
     * @return 结果
     */
    Boolean deleteInterfaceById(Long apiId);

    /**
     * 上线或下线接口
     * @param id 接口ID
     * @param userId 用户
     * @param status 状态
     * @return Boolean
     */
    Boolean updateStatusByOnlineOrOffLine(Long id, Long userId, Boolean status);

    /**
     *  分页获取API接口列表当前开发者
     * @param queryRequest 查询条件
     * @param userId       用户ID
     */
    Page<ApiReleaseResp> getDevInterfaceByPage(ApiQueryRequest queryRequest, Long userId);

    /**
     * 根据分类获取接口列表
     * @param queryRequest 查询条件
     * @param categoryId 分类ID
     * @return 分页结果集
     */
    Page<ApiReleaseResp> selectInterfaceByCategory(ApiQueryRequest queryRequest, Long categoryId);


    /**
     * 统计该开发者上传已通过审核的上线接口数量
     * @param userId 用户ID
     */
    Long getInterfaceByCount(Long userId);

    /**
     * 接口是否存在
     */
    Boolean existsInterfaceById(Long id);

}
