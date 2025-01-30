package cn.apimix.service.impl;

import cn.apimix.mapper.SocialUserMapper;
import cn.apimix.model.entity.SocialUser;
import cn.apimix.model.entity.table.SocialUserTableDef;
import cn.apimix.model.enums.SocialSourceEnum;
import cn.apimix.service.SocialUserService;
import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import me.zhyd.oauth.model.AuthUser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *  服务层实现。
 *
 * @author Hor
 * @since 2024-10-13
 */
@Service
public class SocialUserServiceImpl extends ServiceImpl<SocialUserMapper, SocialUser> implements SocialUserService {

    /**
     * 根据来源和开放 ID 查询
     *
     * @param source 来源
     * @param openId 开放 ID
     * @return 用户社会化关联信息
     */
    @Override
    public SocialUser getBySourceAndOpenId(String source, String openId) {
        return getOne(query()
                .where(SocialUserTableDef.SOCIAL_USER.SOURCE.eq(source))
                .and(SocialUserTableDef.SOCIAL_USER.OPEN_ID.eq(openId))
        );
    }

    /**
     * 绑定
     *
     * @param authUser 三方账号信息
     * @param userId   用户 ID
     */
    @Override
    public Boolean bind(AuthUser authUser, Long userId) {
        String source = authUser.getSource();
        String openId = authUser.getUuid();
        List<SocialUser> userSocialList = this.listByUserId(userId);
        Set<String> boundSocialSet = userSocialList.stream().map(SocialUser::getSource).collect(Collectors.toSet());
        String description = SocialSourceEnum.valueOf(source).getDescription();

        Assert.isFalse(boundSocialSet.contains(source),"您已经绑定过了 [{}] 平台，请先解绑", description);

        SocialUser userSocial = this.getBySourceAndOpenId(source, openId);

        Assert.isNull(userSocial,"[{}] 平台账号 [{}] 已被其他用户绑定",description, authUser.getUsername());

        // 保存
        return save(SocialUser.builder()
                .userId(userId)
                .source(source)
                .openId(openId)
                .metaJson(JSONUtil.toJsonStr(authUser))
                .lastLoginTime(LocalDateTime.now())
                .build());
    }

    /**
     * 根据用户 ID 查询
     *
     * @param userId 用户 ID
     * @return 用户社会化关联信息
     */
    @Override
    public List<SocialUser> listByUserId(Long userId) {
        return list(query().where(SocialUserTableDef.SOCIAL_USER.USER_ID.eq(userId)));
    }

    /**
     * 根据来源和用户 ID 删除
     *
     * @param source 来源
     * @param userId 用户 ID
     */
    @Override
    public Boolean deleteBySourceAndUserId(String source, Long userId) {
        return remove(queryChain()
                .where(SocialUserTableDef.SOCIAL_USER.SOURCE.eq(source))
                .and(SocialUserTableDef.SOCIAL_USER.USER_ID.eq(userId))
        );
    }
}
