package cn.apimix.service;

import cn.apimix.core.core.model.PageRequest;
import cn.apimix.model.dto.token.TokenAddRequest;
import cn.apimix.model.dto.token.TokenEditRequest;
import cn.apimix.model.entity.UserToken;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

/**
 * @Author: Hor
 * @Date: 2024/5/20 20:57
 * @Version: 1.0
 */
public interface IUserTokenService extends IService<UserToken> {

    /**
     * 添加 Token
     *
     * @param addRequest token添加请求信息
     * @param userId     用户ID
     * @return 是否成功
     */
    Boolean saveToken(TokenAddRequest addRequest, Long userId);

    /**
     * 删除 Token
     *
     * @param tokenId tokenId
     * @return 是否成功
     */
    Boolean delToken(Long tokenId, Long userId);


    /**
     * 删除该用户全部token
     *
     * @param userId 用户ID
     * @return 是否成功
     */
    Boolean delUserAllToken(Long userId);

    /**
     * 更新token
     *
     * @param editRequest token
     * @param userId      用户ID
     * @return 是否成功
     */
    Boolean updateToken(TokenEditRequest editRequest, Long userId);


    /**
     * 根据条件分页查询角色数据
     *
     * @param pageRequest 分页
     * @param userId      用户ID
     * @return Token数据集合信息
     */
    Page<UserToken> selectTokenList(PageRequest pageRequest, Long userId);


    /**
     * 根据 TokenID 和 用户ID获取
     *
     * @param id     tokenId
     * @param userId 用户ID
     * @return Token信息
     */
    UserToken selectTokenByIdAndUserId(Long id, Long userId);


    /**
     * 根据`TokenValue`获取
     *
     * @param tokenValue Token值
     */
    UserToken selectTokenByTokenValue(String tokenValue);

}
