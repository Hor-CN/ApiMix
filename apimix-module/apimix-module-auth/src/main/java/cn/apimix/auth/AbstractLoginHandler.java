package cn.apimix.auth;

import cn.apimix.auth.model.req.LoginReq;
import cn.apimix.model.entity.User;
import cn.apimix.service.CaptchaService;
import cn.apimix.service.SocialUserService;
import cn.apimix.service.UserService;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 登录处理器基类
 *
 * @Author: Hor
 * @Date: 2025/1/29 11:43
 * @Version: 1.0
 */
@Component
public abstract class AbstractLoginHandler<T extends LoginReq> implements LoginHandler<T> {

    @Resource
    protected UserService userService;

    @Resource
    protected CaptchaService captchaService;

    @Resource
    protected SocialUserService socialUserService;


    @Override
    public void preLogin(T req, HttpServletRequest request) {}

    @Override
    public void postLogin(T req, HttpServletRequest request) {}
    /**
     * 认证
     *
     * @param user   用户信息
     * @return token 令牌信息
     */
    protected String authenticate(User user) {
        StpUtil.login(user.getId());
        // 返回用户Token
        return StpUtil.getTokenValue();
    }


    public void checkUserStatus(User user) {
        // 判断用户状态是否处于禁用状态
        if (user.getStatus() == 0) {
            StpUtil.disable(user.getId(), -1);
        } else if (StpUtil.isDisable(user.getId())) {
            StpUtil.untieDisable(user.getId());
        }
        Assert.isFalse(
                StpUtil.isDisable(user.getId()),
                "账号已被封禁" + StpUtil.getDisableTime(user.getId())
        );
    }
}
