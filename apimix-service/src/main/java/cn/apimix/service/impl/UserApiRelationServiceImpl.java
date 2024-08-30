package cn.apimix.service.impl;

import cn.apimix.core.core.model.PageRequest;
import cn.apimix.mapper.UserApiRelationMapper;
import cn.apimix.model.entity.UserApiRelation;
import cn.apimix.model.entity.table.ApiInfoTableDef;
import cn.apimix.model.entity.table.UserApiRelationTableDef;
import cn.apimix.model.vo.api.ApiRelationVo;
import cn.apimix.service.IUserApiRelationService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 服务层实现。
 *
 * @author Hor
 * @since 2024-06-17
 */
@Service
public class UserApiRelationServiceImpl extends ServiceImpl<UserApiRelationMapper, UserApiRelation> implements IUserApiRelationService {

    @Override
    public Page<ApiRelationVo> selectUserApiRelationByPage(PageRequest pageRequest, Long userId) {

        return pageAs(Page.of(pageRequest.getPageNumber(), pageRequest.getPageSize()),
                query().select(
                                UserApiRelationTableDef.USER_API_RELATION.ALL_COLUMNS,
                                ApiInfoTableDef.API_INFO.NAME,
                                ApiInfoTableDef.API_INFO.LOGO
                        ).from(UserApiRelationTableDef.USER_API_RELATION)
                        .where(UserApiRelationTableDef.USER_API_RELATION.USER_ID.eq(userId))
                        .leftJoin(ApiInfoTableDef.API_INFO).on(UserApiRelationTableDef.USER_API_RELATION.API_ID.eq(ApiInfoTableDef.API_INFO.ID))
                , ApiRelationVo.class);
    }

    /**
     * 判断用户是否申请有接口
     *
     * @param apiId  接口ID
     * @param userId 用户ID
     */
    @Override
    public Boolean isUserApiRelationExist(Long apiId, Long userId) {
        return exists(query()
                .where(UserApiRelationTableDef.USER_API_RELATION.API_ID.eq(apiId))
                .and(UserApiRelationTableDef.USER_API_RELATION.USER_ID.eq(userId))
        );
    }

    /**
     * 获取用户接口信息
     */

    public UserApiRelation selectApiRelation(Long apiId, Long userId) {
        return getOne(query()
                .where(UserApiRelationTableDef.USER_API_RELATION.API_ID.eq(apiId))
                .and(UserApiRelationTableDef.USER_API_RELATION.USER_ID.eq(userId))
        );
    }
}
