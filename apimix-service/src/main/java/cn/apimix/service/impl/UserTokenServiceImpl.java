package cn.apimix.service.impl;

import cn.apimix.core.core.model.PageRequest;
import cn.apimix.mapper.UserTokenMapper;
import cn.apimix.model.dto.token.TokenAddRequest;
import cn.apimix.model.dto.token.TokenEditRequest;
import cn.apimix.model.entity.UserToken;
import cn.apimix.model.entity.table.UserTokenTableDef;
import cn.apimix.service.IUserTokenService;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 服务层实现。
 *
 * @Author: Hor
 * @Date: 2024/5/20 21:47
 * @Version: 1.0
 */
@Service
public class UserTokenServiceImpl extends ServiceImpl<UserTokenMapper, UserToken> implements IUserTokenService {
    /**
     * 添加 Token
     *
     * @param addRequest token实体
     * @return 是否成功
     */
    @Override
    public Boolean saveToken(TokenAddRequest addRequest, Long userId) {
        // 保存 Token 信息
        UserToken userToken = UserToken.builder()
                .userId(userId)
                .tokenValue(IdUtil.simpleUUID())
                .remark(addRequest.getRemark())
                .expired(addRequest.getExpired())
                .build();
        // 返回保存结果
        return save(userToken);
    }

    /**
     * 删除 Token
     *
     * @param tokenId tokenID
     * @param userId  用户ID
     * @return 是否成功
     */
    @Override
    public Boolean delToken(Long tokenId, Long userId) {
        // 判断是否是当前用户的Token
        boolean exists = queryChain()
                .where(UserTokenTableDef.USER_TOKEN.ID.eq(tokenId)
                        .and(UserTokenTableDef.USER_TOKEN.USER_ID.eq(userId))).exists();
        Assert.isTrue(exists, "非法操作");
        // 删除Token
        return removeById(tokenId);
    }

    /**
     * 删除该用户全部token
     *
     * @param userId 用户ID
     * @return 是否成功
     */
    @Override
    public Boolean delUserAllToken(Long userId) {
        return remove(QueryWrapper.create()
                .where(UserTokenTableDef.USER_TOKEN.USER_ID.eq(userId)));
    }

    /**
     * 更新token
     *
     * @param editRequest token
     * @return 是否成功
     */
    @Override
    public Boolean updateToken(TokenEditRequest editRequest, Long userId) {
        // 判断是否是当前用户的Token
        boolean exists = queryChain().where(
                UserTokenTableDef.USER_TOKEN.ID.eq(editRequest.getId())
                        .and(
                                UserTokenTableDef.USER_TOKEN.USER_ID.eq(userId)
                        )
        ).exists();
        Assert.isTrue(exists, "非法操作");
        UserToken token = UserToken.builder()
                .id(editRequest.getId())
                .userId(userId)
                .expired(editRequest.getExpired())
                .remark(editRequest.getRemark())
                .build();
        return updateById(token);
    }

    /**
     * 根据条件分页查询角色数据
     *
     * @param pageRequest 分页
     * @param userId      用户ID
     * @return Token数据集合信息
     */
    @Override
    public Page<UserToken> selectTokenList(PageRequest pageRequest, Long userId) {
        // 构建分页
        Page<UserToken> page = Page.of(pageRequest.getPageNumber(), pageRequest.getPageSize());

        // 根据用户ID获取分页数据
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(UserTokenTableDef.USER_TOKEN.USER_ID.eq(userId));

        return page(page, queryWrapper);
    }

    /**
     * 根据 TokenID 和 用户ID获取
     *
     * @param id     tokenId
     * @param userId 用户ID
     * @return Token信息
     */
    @Override
    public UserToken selectTokenByIdAndUserId(Long id, Long userId) {
        // 判断是否是当前用户的Token
        boolean exists = queryChain().where(
                UserTokenTableDef.USER_TOKEN.ID.eq(id)
                        .and(UserTokenTableDef.USER_TOKEN.USER_ID.eq(userId))
        ).exists();

        Assert.isTrue(exists, "非法授权");
        // 返回结果
        return getById(id);
    }

    /**
     * 根据`TokenValue`获取
     *
     * @param tokenValue Token值
     */
    @Override
    public UserToken selectTokenByTokenValue(String tokenValue) {
        return getOne(query().where(
                UserTokenTableDef.USER_TOKEN.TOKEN_VALUE.eq(tokenValue)
        ));
    }
}
