package cn.apimix.user.service;

import cn.apimix.user.model.entity.SocialUser;
import com.mybatisflex.core.service.IService;
import me.zhyd.oauth.model.AuthUser;

import java.util.List;

/**
 *  服务层。
 *
 * @author Hor
 * @since 2024-10-13
 */
public interface SocialUserService extends IService<SocialUser> {


    /**
     * 根据来源和开放 ID 查询
     *
     * @param source 来源
     * @param openId 开放 ID
     * @return 用户社会化关联信息
     */
    SocialUser getBySourceAndOpenId(String source, String openId);


    /**
     * 绑定
     *
     * @param authUser 三方账号信息
     * @param userId   用户 ID
     */
    Boolean bind(AuthUser authUser, Long userId);


    /**
     * 根据用户 ID 查询
     *
     * @param userId 用户 ID
     * @return 用户社会化关联信息
     */
    List<SocialUser> listByUserId(Long userId);


    /**
     * 根据来源和用户 ID 删除
     *
     * @param source 来源
     * @param userId 用户 ID
     */
    Boolean deleteBySourceAndUserId(String source, Long userId);

}
