package cn.apimix.service.impl;

import cn.apimix.mapper.ApiTokenMapper;
import cn.apimix.model.entity.ApiToken;
import cn.apimix.model.entity.table.ApiTokenTableDef;
import cn.apimix.service.IApiTokenService;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Author: Hor
 * @Date: 2024/8/8 下午9:42
 * @Version: 1.0
 */
@Service
public class ApiTokenServiceImpl extends ServiceImpl<ApiTokenMapper, ApiToken> implements IApiTokenService {


    @Override
    public ApiToken getApiTokenByTokenIdAndApiId(Long tokenId, Long apiId) {
        return getOne(query().where(
                ApiTokenTableDef.API_TOKEN.TOKEN_ID.eq(tokenId).and(
                ApiTokenTableDef.API_TOKEN.API_ID.eq(apiId)))
        );
    }

    @Override
    public Boolean increaseTheNumberOfCalls(Long id) {
        return UpdateChain.of(ApiToken.class)
                .setRaw(ApiToken::getUsedQuota, "used_quota + 1")
                .where(ApiToken::getId).eq(id)
                .update();
    }
}
