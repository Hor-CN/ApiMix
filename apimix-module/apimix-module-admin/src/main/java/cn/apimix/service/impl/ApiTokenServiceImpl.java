package cn.apimix.service.impl;

import cn.apimix.mapper.ApiTokenMapper;
import cn.apimix.mapper.UserTokenMapper;
import cn.apimix.model.dto.token.AllocationTokenEditRequest;
import cn.apimix.model.entity.ApiToken;
import cn.apimix.model.entity.table.ApiTokenTableDef;
import cn.apimix.model.entity.table.UserTokenTableDef;
import cn.apimix.model.vo.api.AllocationTokenVO;
import cn.apimix.service.ApiTokenService;
import cn.hutool.core.lang.Assert;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/8/8 下午9:42
 * @Version: 1.0
 */
@Service
public class ApiTokenServiceImpl extends ServiceImpl<ApiTokenMapper, ApiToken> implements ApiTokenService {


    @Resource
    private UserTokenMapper userTokenMapper;

    @Override
    public ApiToken getApiTokenByTokenIdAndApiId(Long tokenId, Long apiId) {
        return getOne(query().where(
                ApiTokenTableDef.API_TOKEN.TOKEN_ID.eq(tokenId)
                        .and(ApiTokenTableDef.API_TOKEN.API_ID.eq(apiId))
                )
        );
    }

    @Override
    public Boolean increaseTheNumberOfCalls(Long id) {
        return UpdateChain.of(ApiToken.class)
                .setRaw(ApiToken::getUsedQuota, "used_quota + 1")
                .where(ApiToken::getId).eq(id)
                .update();
    }

    /**
     * 获取某用户给某接口分配的Token情况
     *
     * @param userId 用户ID
     * @param apiId  接口ID
     */
    @Override
    public List<AllocationTokenVO> getUserAllocationTokens(Long userId, Long apiId) {
        return listAs(query().select(
                        ApiTokenTableDef.API_TOKEN.ID,
                        UserTokenTableDef.USER_TOKEN.NAME,
                        UserTokenTableDef.USER_TOKEN.STATUS,
                        UserTokenTableDef.USER_TOKEN.TOKEN_VALUE,
                        UserTokenTableDef.USER_TOKEN.REMARK,
                        UserTokenTableDef.USER_TOKEN.EXPIRED,
                        ApiTokenTableDef.API_TOKEN.TOTAL_QUOTA,
                        ApiTokenTableDef.API_TOKEN.USED_QUOTA,
                        ApiTokenTableDef.API_TOKEN.IS_UNLIMITED,
                        ApiTokenTableDef.API_TOKEN.CREATE_TIME
                ).join(UserTokenTableDef.USER_TOKEN)
                .on(UserTokenTableDef.USER_TOKEN.ID.eq(ApiTokenTableDef.API_TOKEN.TOKEN_ID)),AllocationTokenVO.class);
    }

    /**
     * 获取某分配的Token信息
     *
     * @param id     主键ID
     * @param userId 用户ID
     */
    @Override
    public ApiToken getApiTokenByIdAndUserId(Long id, Long userId) {
        return getOne(query()
                .where(ApiTokenTableDef.API_TOKEN.ID.eq(id))
                .and(ApiTokenTableDef.API_TOKEN.USER_ID.eq(userId))
        );
    }

    /**
     * 重置已用额度
     *
     * @param id     id
     * @param userId 用户Id
     */
    @Override
    public Boolean restAllocationTokenUsedQuota(Long id, Long userId) {
        boolean exists = exists(query().where(ApiTokenTableDef.API_TOKEN.ID.eq(id)).and(ApiTokenTableDef.API_TOKEN.USER_ID.eq(userId)));
        Assert.isTrue(exists, "重置失败");
        return updateById(ApiToken.builder()
            .id(id)
            .usedQuota(0L)
            .build());
    }

    /**
     * 删除
     *
     * @param id
     * @param userId
     */
    @Override
    public Boolean removeApiTokenByIdAndUserId(Long id, Long userId) {
        return remove(query().where(ApiTokenTableDef.API_TOKEN.ID.eq(id))
                .and(ApiTokenTableDef.API_TOKEN.USER_ID.eq(userId)));
    }

    /**
     * 修改分配的Token
     *
     * @param editRequest    AllocationTokenEditRequest
     *
     */
    @Override
    public Boolean updateApiTokenByUserIdAndApiIdAndTokenId(AllocationTokenEditRequest editRequest, Long userId) {

        return update(ApiToken.builder()
                .id(editRequest.getId())
                .userId(userId)
                .apiId(editRequest.getApiId())
                .tokenId(editRequest.getTokenId())
                .totalQuota(editRequest.getTotalQuota())
                        .build(),
                query().where(ApiTokenTableDef.API_TOKEN.ID.eq(editRequest.getId()))
                        .and(ApiTokenTableDef.API_TOKEN.TOKEN_ID.eq(editRequest.getTokenId()))
                        .and(ApiTokenTableDef.API_TOKEN.API_ID.eq(editRequest.getApiId()))
                        .and(ApiTokenTableDef.API_TOKEN.USER_ID.eq(userId))
        );
    }


}
