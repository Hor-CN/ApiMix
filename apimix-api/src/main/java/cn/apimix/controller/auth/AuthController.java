package cn.apimix.controller.auth;

import cn.apimix.common.resp.Result;
import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.core.exception.HorApiException;
import cn.apimix.user.model.entity.Role;
import cn.apimix.user.model.entity.User;
import cn.apimix.user.model.mapstruct.UserMapping;
import cn.apimix.user.model.req.LoginReq;
import cn.apimix.user.model.req.UserRegisterRequest;
import cn.apimix.user.model.req.system.SysUserAddRequest;
import cn.apimix.user.model.resp.LoginResp;
import cn.apimix.user.model.resp.RouteResp;
import cn.apimix.user.model.resp.SocialAuthAuthorizeResp;
import cn.apimix.user.model.resp.user.UserInfoResp;
import cn.apimix.user.service.AuthService;
import cn.apimix.user.service.UserService;
import cn.apimix.user.service.impl.CaptchaServiceImpl;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Hor
 * @Date: 2024/12/15 23:33
 * @Version: 1.0
 */
@Slf4j
@RestController
@ResponseResult
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    private final UserMapping userMapping;

    private final AuthService authService;

    private final AuthRequestFactory authRequestFactory;

    private final CaptchaServiceImpl captchaService;


    @PostMapping("/register")
    public Boolean register(@Validated @RequestBody UserRegisterRequest registerReq) {
        // 校验验证码
        captchaService.checkEmailCode(registerReq.getEmail(), registerReq.getCaptcha());
        // 校验密码
        Assert.isTrue(registerReq.getPassWord().equals(registerReq.getRepeatPassword()), "两次密码不一致");

        return userService.insertUser(SysUserAddRequest.builder()
                .username(registerReq.getUserName())
                .nickname(registerReq.getUserName())
                .email(registerReq.getEmail())
                .password(registerReq.getPassWord())
                .status(1)
                .description("还没有个性签名呢")
                .roleIds(Collections.singletonList(4))
                .build());
    }



    @PostMapping("/login")
    public LoginResp login(@Validated @RequestBody LoginReq req, HttpServletRequest request) {
        return authService.login(req, request);
    }

    /**
     * 注销登录
     *
     * @return 结果
     */
    @PostMapping("logout")
    public Result<?> logout() {
        StpUtil.logout();
        return Result.buildSuccess("注销登录成功");
    }

    @GetMapping("/{source}")
    public SocialAuthAuthorizeResp authorize(@PathVariable String source) {
        AuthRequest authRequest = this.getAuthRequest(source);
        String authorize = authRequest.authorize(AuthStateUtils.createState());
        log.info("第三方登录地址获取：{}", authorize);
        return SocialAuthAuthorizeResp.builder()
                .authorizeUrl(authorize)
                .build();
    }

    /**
     * 获取当前用户信息
     *
     * @return UserVo 用户信息
     */
    @SaCheckLogin
    @GetMapping("/user/info")
    public UserInfoResp getUserInfo() {
        // 获取当前用户
        User user = userService.selectUserById(StpUtil.getLoginIdAsLong());
        UserInfoResp userInfoResp = userMapping.userToUserVo(user);
        List<String> roleNames = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        // 填充用户角色
        userInfoResp.setRoleNames(roleNames);
        userInfoResp.setRoles(StpUtil.getRoleList());
        // 填充用户权限
        userInfoResp.setPermission(StpUtil.getPermissionList());
        return userInfoResp;
    }

    /**
     * 构建当前用户的菜单路由
     */
    @SaCheckLogin
    @GetMapping("routes")
    public List<RouteResp> buildMenuListRoutes() {
        return authService.buildRouteTree(StpUtil.getLoginIdAsLong());
    }

    private AuthRequest getAuthRequest(String source) {
        try {
            return authRequestFactory.get(source);
        } catch (Exception e) {
            throw new HorApiException(String.format("暂不支持 [%s] 平台账号登录", source));
        }
    }

}
