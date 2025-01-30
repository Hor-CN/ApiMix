package cn.apimix.auth.handler;

import cn.apimix.auth.AbstractLoginHandler;
import cn.apimix.auth.enunms.AuthTypeEnum;
import cn.apimix.auth.model.req.SocialLoginReq;
import cn.apimix.auth.model.resp.LoginResp;
import cn.apimix.core.exception.HorApiException;
import cn.apimix.model.entity.SocialUser;
import cn.apimix.model.entity.User;
import cn.apimix.model.entity.UserAccount;
import cn.apimix.service.UserAccountService;
import cn.apimix.service.UserRoleService;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;

/**
 * @Author: Hor
 * @Date: 2025/1/29 15:26
 * @Version: 1.0
 */
@Component
@RequiredArgsConstructor
public class SocialLoginHandler extends AbstractLoginHandler<SocialLoginReq> {

    private final AuthRequestFactory authRequestFactory;


    private final UserRoleService userRoleService;

    private final UserAccountService userAccountService;


    /**
     * 登录
     *
     * @param req     登录请求参数
     * @param request 请求对象
     * @return 登录响应参数
     */
    @Override
    @Transactional
    public LoginResp login(SocialLoginReq req, HttpServletRequest request) {
        // 获取第三方登录信息
        AuthRequest authRequest = this.getAuthRequest(req.getSource());
        AuthCallback callback = new AuthCallback();
        callback.setCode(req.getCode());
        callback.setState(req.getState());
        AuthResponse<AuthUser> response = authRequest.login(callback);
        // 响应校验
        Assert.isTrue(response.ok(), response.getMsg());
        AuthUser authUser = response.getData();
        // 如未绑定则自动注册新用户，保存或更新关联信息
        String source = authUser.getSource();
        String openId = authUser.getUuid();
        SocialUser userSocial = socialUserService.getBySourceAndOpenId(source, openId);
        User user;
        // 未注册
        if (null == userSocial) {
            String nickname = authUser.getNickname();
            // 用户名已存在时生成随机用户名
            String username = userService.existsUserGenUserName(authUser.getUsername());

            user = User.builder()
                    .userName(username)
                    .nickName(nickname)
                    .status(1)
                    .avatar(authUser.getAvatar())
                    .build();
            // 新建用户
            userService.save(user);
            Long userId = user.getId();

            // 分配用户角色
            userRoleService.insertBatchUserRole(userId, Collections.singletonList(4));
            userSocial = SocialUser.builder()
                    .userId(userId)
                    .source(source)
                    .openId(openId)
                    .build();

            // 生成账户
            userAccountService.save(UserAccount.builder()
                    .userId(userId)
                    .build());
        } else {
            user = userService.getById(userSocial.getUserId());
        }
        // 检查用户状态
        super.checkUserStatus(user);
        userSocial.setMetaJson(JSONUtil.toJsonStr(authUser));
        userSocial.setLastLoginTime(LocalDateTime.now());
        socialUserService.saveOrUpdate(userSocial);
        // 执行认证
        String token = super.authenticate(user);
        return LoginResp.builder()
                .id(user.getId())
                .token(token).build();
    }

    @Override
    public void preLogin(SocialLoginReq req, HttpServletRequest request) {
        super.preLogin(req, request);
        // 如果登录，退出登录
        if (StpUtil.isLogin()) {
            StpUtil.logout();
        }
    }

    /**
     * 获取 AuthRequest
     *
     * @param source 平台名称
     * @return AuthRequest
     */
    private AuthRequest getAuthRequest(String source) {
        try {
            return authRequestFactory.get(source);
        } catch (Exception e) {
            throw new HorApiException(String.format("暂不支持 [%s] 平台账号登录", source));
        }
    }


    /**
     * 获取认证类型
     * .
     *
     * @return 认证类型
     */
    @Override
    public AuthTypeEnum getAuthType() {
        return AuthTypeEnum.SOCIAL;
    }
}
