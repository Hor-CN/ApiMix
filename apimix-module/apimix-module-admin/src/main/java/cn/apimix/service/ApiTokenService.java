package cn.apimix.service;

import cn.apimix.model.dto.token.AllocationTokenEditRequest;
import cn.apimix.model.entity.ApiToken;
import cn.apimix.model.vo.api.AllocationTokenVO;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/8/8 下午9:41
 * @Version: 1.0
 */
public interface ApiTokenService extends IService<ApiToken> {


    /**
     * 根据TokenId和接口Id获取分配的情况
     * @param tokenId TokenId
     * @param apiId 接口ID
     */
    ApiToken getApiTokenByTokenIdAndApiId(Long tokenId,Long apiId);


    /**
     * 调用次数增加
     * @param id 主键Id
     * @return 增加结果
     */
    Boolean increaseTheNumberOfCalls(Long id);

    /**
     * 获取某用户给某接口分配的Token情况
     * @param userId 用户ID
     * @param apiId 接口ID
     */
    List<AllocationTokenVO> getUserAllocationTokens(Long userId, Long apiId);



    /**
     * 获取某分配的Token信息
     * @param id 主键ID
     * @param userId 用户ID
     */
    ApiToken getApiTokenByIdAndUserId(Long id, Long userId);


    /**
     * 重置已用额度
     * @param id id
     */
    Boolean restAllocationTokenUsedQuota(Long id, Long userId);


    /**
     * 删除
     */
    Boolean removeApiTokenByIdAndUserId(Long id, Long userId);


    /**
     * 修改分配的Token
     * @param editRequest AllocationTokenEditRequest
     * @param userId 用户ID
     */
    Boolean updateApiTokenByUserIdAndApiIdAndTokenId(AllocationTokenEditRequest editRequest, Long userId);

}
