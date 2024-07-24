package cn.apimix.service;

import cn.apimix.core.core.model.PageRequest;
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
public interface IUserApiRelationService extends IService<UserApiRelation> {


    Page<ApiRelationVo> selectUserApiRelationByPage(PageRequest pageRequest, Long userId);


    /**
     * 判断用户是否申请有接口
     *
     * @param apiId  接口ID
     * @param userId 用户ID
     */
    Boolean isUserApiRelationExist(Long apiId, Long userId);

}
