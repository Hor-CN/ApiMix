package cn.apimix.user.handler;

import cn.apimix.user.AbstractLoginHandler;
import cn.apimix.user.enunms.AuthTypeEnum;
import cn.apimix.user.enunms.SocialSourceEnum;
import cn.apimix.user.model.entity.SocialUser;
import cn.apimix.user.model.entity.User;
import cn.apimix.user.model.entity.table.SocialUserTableDef;
import cn.apimix.user.model.entity.table.UserTableDef;
import cn.apimix.user.model.resp.LoginResp;
import cn.apimix.user.model.req.WxGzhLoginReq;
import cn.apimix.core.config.RedisKeyConstants;
import cn.apimix.core.utils.RedisUtils;
import cn.hutool.core.lang.Assert;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @Author: Hor
 * @Date: 2025/1/29 15:11
 * @Version: 1.0
 */
@Component
public class WxLoginHandler extends AbstractLoginHandler<WxGzhLoginReq> {
    /**
     * 登录
     *
     * @param req     登录请求参数
     * @param request 请求对象
     * @return 登录响应参数
     */
    @Override
    public LoginResp login(WxGzhLoginReq req, HttpServletRequest request) {
        String captchaKey = RedisKeyConstants.WX_CAPTCHA_KEY_PREFIX + req.getCaptcha();
        String uuid = RedisUtils.get(captchaKey);
        // 使用后删除
        RedisUtils.delete(captchaKey);
        // 验证码已失效
        Assert.notBlank(uuid, "验证码已失效");

        // 拿到uuid与数据库的三方登录关联表对比，没有报错，有登录
        SocialUser socialUser = socialUserService.getOne(QueryWrapper.create()
                .where(SocialUserTableDef.SOCIAL_USER.OPEN_ID.eq(uuid))
                .and(SocialUserTableDef.SOCIAL_USER.SOURCE.eq(SocialSourceEnum.WECHAT))
        );

        Assert.notNull(socialUser, "此微信未绑定本系统账号");

        User user = userService.queryChain().where(UserTableDef.USER.ID.eq(socialUser.getUserId())).one();
        this.checkUserStatus(user);
        // 更新最近登录时间
        socialUser.setLastLoginTime(LocalDateTime.now());
        socialUserService.saveOrUpdate(socialUser);

        // 执行认证
        String token = this.authenticate(user);
        return LoginResp.builder()
                .id(user.getId())
                .token(token).build();
    }

    /**
     * 获取认证类型
     * .
     *
     * @return 认证类型
     */
    @Override
    public AuthTypeEnum getAuthType() {
        return AuthTypeEnum.WX;
    }
}
