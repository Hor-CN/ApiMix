package cn.apimix.service;

import cn.apimix.model.entity.ApiToken;
import com.mybatisflex.core.service.IService;

/**
 * @Author: Hor
 * @Date: 2024/8/8 下午9:41
 * @Version: 1.0
 */
public interface IApiTokenService extends IService<ApiToken> {

    ApiToken getApiTokenByTokenIdAndApiId(Long tokenId,Long apiId);


    Boolean increaseTheNumberOfCalls(Long id);
}
