package cn.apimix.controller.console;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.core.config.RedisKeyConstants;
import cn.apimix.core.utils.RedisUtils;
import cn.apimix.model.dto.user.*;
import cn.apimix.model.entity.Audit;
import cn.apimix.model.entity.SocialUser;
import cn.apimix.model.enums.SocialSourceEnum;
import cn.apimix.model.vo.user.UserSocialBindResp;
import cn.apimix.service.SocialUserService;
import cn.apimix.service.UserService;
import cn.apimix.service.impl.AuditServiceImpl;
import cn.apimix.service.impl.CaptchaServiceImpl;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Hor
 * @Date: 2024/12/16 18:43
 * @Version: 1.0
 */

@RestController
@ResponseResult
@RequiredArgsConstructor
@RequestMapping("/api/console/account")
public class AccountController {

    private final CaptchaServiceImpl captchaService;

    private final AuditServiceImpl auditService;

    private final UserService userService;

    private final SocialUserService socialUserService;

    private final AuthRequestFactory authRequestFactory;


    /**
     * 查询绑定的三方账号
     */
    @SaCheckLogin
    @GetMapping("/social")
    public List<UserSocialBindResp> listSocialBind() {
        List<SocialUser> userSocialList = socialUserService.listByUserId(StpUtil.getLoginIdAsLong());
        return userSocialList.stream().map(userSocial -> {
            String source = userSocial.getSource();
            UserSocialBindResp userSocialBind = new UserSocialBindResp();
            userSocialBind.setSource(source);
            userSocialBind.setDescription(SocialSourceEnum.valueOf(source).getDescription());
            return userSocialBind;
        }).collect(Collectors.toList());
    }

    /**
     * 解绑三方账号
     */
    @SaCheckLogin
    @DeleteMapping("/social/{source}")
    public void unbindSocial(@PathVariable String source) {
        socialUserService.deleteBySourceAndUserId(source, StpUtil.getLoginIdAsLong());
    }

    /**
     * 绑定账户
     * @param source 来源
     * @param callback 回调
     */
    @SaCheckLogin
    @PostMapping("/social/{source}")
    public void bindSocial(@PathVariable String source, @RequestBody AuthCallback callback) {
        AuthRequest authRequest = authRequestFactory.get(source);
        AuthResponse<AuthUser> response = authRequest.login(callback);
        Assert.isTrue(response.ok(), response.getMsg());
        AuthUser authUser = response.getData();
        socialUserService.bind(authUser, StpUtil.getLoginIdAsLong());
    }


    @PostMapping("/bindWxLogin")
    public Boolean bindWxLogin(@RequestBody
                               @Validated
                               WxLoginRequest loginRequest) {
        String captchaKey = RedisKeyConstants.WX_CAPTCHA_KEY_PREFIX + loginRequest.getCaptcha();
        String uuid = RedisUtils.get(captchaKey);
        // 使用后删除
        RedisUtils.delete(captchaKey);

        // 验证码已失效
        Assert.notBlank(uuid, "验证码已失效");
        Long userId = StpUtil.getLoginIdAsLong();
        return socialUserService.bind(AuthUser.builder()
                        .uuid(uuid)
                        .source(SocialSourceEnum.WECHAT.name())
                .build(),userId);
    }

    /**
     * 修改邮箱
     * @param updateReq updateReq
     */
    @PatchMapping("/email")
    public Boolean updateEmail(@Validated @RequestBody UserEmailUpdateRequest updateReq) {
        captchaService.checkEmailCode(updateReq.getEmail(), updateReq.getCaptcha());
        return  userService.updateEmail(updateReq.getEmail(), updateReq.getOldPassword(), StpUtil.getLoginIdAsLong());
    }


    /**
     * 修改用户基本信息
     */
    @SaCheckLogin
    @PatchMapping("/basic/info")
    public Boolean editUser(@RequestBody UserEditRequest editRequest) {
        return userService.updateBasicInfo(editRequest, StpUtil.getLoginIdAsLong());
    }

    /**
     * 修改密码
     */
    @SaCheckLogin
    @PatchMapping("/password")
    public void updatePassword(@Validated @RequestBody UserPasswordUpdateRequest updateReq) {
        userService.resetUserPassword(StpUtil.getLoginIdAsLong(), updateReq.getOldPassword(), updateReq.getNewPassword());
        // 修改后登出
        StpUtil.logout();
    }


    /**
     * 开发者认证提交
     */
    @SaCheckLogin
    @PostMapping("/dev")
    public Boolean applyDeveloper(@Validated @RequestBody ApplyDeveloperRequest request) {
        captchaService.checkEmailCode(request.getEmail(), request.getCaptcha());
        return auditService.insertAudit(Audit.builder()
                .flowNo(StpUtil.getLoginIdAsLong())
                .type(2)
                .status(1)
                .build());
    }

    /**
     * 获取开发者申请的状态
     */
    @SaCheckLogin
    @GetMapping("/devStatus")
    public Audit getDevStatus() {
        return auditService.selectAuditStatus(StpUtil.getLoginIdAsLong(), 2);
    }



}
