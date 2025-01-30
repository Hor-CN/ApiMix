package cn.apimix.service;

import cn.apimix.model.dto.api.ApiInfoQueryRequest;
import cn.apimix.model.entity.UserApiRelation;
import cn.apimix.model.vo.api.ApiRelationVo;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

/**
 * 服务层。
 *
 * @author Hor
 * @since 2024-06-17
 */
public interface UserApiRelationService extends IService<UserApiRelation> {


    Page<ApiRelationVo> selectUserApiRelationByPage(ApiInfoQueryRequest pageRequest, Long userId);


    /**
     * 判断用户是否申请有接口
     *
     * @param apiId  接口ID
     * @param userId 用户ID
     */
    Boolean isUserApiRelationExist(Long apiId, Long userId);

}
