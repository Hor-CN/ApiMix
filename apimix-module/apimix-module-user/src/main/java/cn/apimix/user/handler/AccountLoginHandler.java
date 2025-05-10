package cn.apimix.user.handler;

import cn.apimix.core.utils.RedisUtils;
import cn.apimix.user.AbstractLoginHandler;
import cn.apimix.user.enunms.AuthTypeEnum;
import cn.apimix.user.model.entity.User;
import cn.apimix.user.model.entity.table.UserTableDef;
import cn.apimix.user.model.req.AccountLoginReq;
import cn.apimix.user.model.resp.LoginResp;
import cn.apimix.core.constant.CacheConstants;
import cn.dev33.satoken.secure.SaSecureUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.servlet.ServletUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;

/**
 * 账号登录处理器
 *
 * @Author: Hor
 * @Date: 2025/1/29 11:47
 * @Version: 1.0
 */
@Component
@RequiredArgsConstructor
public class AccountLoginHandler extends AbstractLoginHandler<AccountLoginReq> {

    /**
     * 登录
     *
     * @param req     登录请求参数
     * @param request 请求对象
     * @return 登录响应参数
     */
    @Override
    public LoginResp login(AccountLoginReq req, HttpServletRequest request) {
        // 用户不存在
        boolean exists = userService.queryChain().where(UserTableDef.USER.USER_NAME.eq(req.getUsername())).exists();
        Assert.isTrue(exists, "账号或密码错误，登录失败");
        // 获取用户
        User user = userService.queryChain().where(UserTableDef.USER.USER_NAME.eq(req.getUsername())).one();
        boolean isError = ObjectUtil.isNull(user) || !user.getPassword().equals(SaSecureUtil.md5(
                SaSecureUtil.md5(req.getPassword()) + SaSecureUtil.md5(user.getSalt())));
        // 判断密码是否相等
        Assert.isFalse(isError, "账号或密码错误，登录失败");
        // 检查账号锁定状态
        this.checkUserLocked(req.getUsername(), request, isError);
        // 检查用户状态
        super.checkUserStatus(user);
        // 执行认证
        String token = this.authenticate(user);
        return LoginResp.builder()
                .id(user.getId())
                .token(token).build();

    }

    /**
     * 登录前置处理
     *
     * @param req     登录请求参数
     * @param request 请求对象
     */
    @Override
    public void preLogin(AccountLoginReq req, HttpServletRequest request) {
        super.preLogin(req, request);
        // 校验验证码
        captchaService.checkImageCaptcha(req.getUuid(), req.getCaptcha());
    }


    /**
     * 获取认证类型
     * .
     *
     * @return 认证类型
     */
    @Override
    public AuthTypeEnum getAuthType() {
        return AuthTypeEnum.ACCOUNT;
    }

    /**
     * 检测用户是否已被锁定
     *
     * @param username 用户名
     * @param request  请求对象
     * @param isError  是否登录错误
     */
    private void checkUserLocked(String username, HttpServletRequest request, boolean isError) {
        // 检测是否已被锁定
        String key = CacheConstants.USER_PASSWORD_ERROR_KEY_PREFIX + RedisUtils.formatKey(username, ServletUtil.getClientIP(request));
        Integer currentErrorCount = ObjectUtil.defaultIfNull(RedisUtils.get(key), 0);
        Assert.isFalse(currentErrorCount >= 5, "账号锁定 {} 分钟，请稍后再试", 5);
        // 登录成功清除计数
        if (!isError) {
            RedisUtils.delete(key);
            return;
        }
        // 登录失败递增计数
        currentErrorCount++;
        RedisUtils.set(key, currentErrorCount, Duration.ofMinutes(5));
        Assert.isFalse(currentErrorCount >= 5, "密码错误已达 {} 次，账号锁定 {} 分钟", 5, 5);
    }


}
