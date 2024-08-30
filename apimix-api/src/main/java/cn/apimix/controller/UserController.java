package cn.apimix.controller;

import cn.apimix.RedisUtils;
import cn.apimix.common.resp.Result;
import cn.apimix.config.CacheConstants;
import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.core.core.model.IdRequest;
import cn.apimix.model.dto.user.*;
import cn.apimix.model.entity.Audit;
import cn.apimix.model.entity.Menu;
import cn.apimix.model.entity.Role;
import cn.apimix.model.entity.User;
import cn.apimix.model.mapstruct.UserMapping;
import cn.apimix.model.vo.user.UserLoginVo;
import cn.apimix.model.vo.user.UserVo;
import cn.apimix.service.impl.AuditServiceImpl;
import cn.apimix.service.impl.MenuServiceImpl;
import cn.apimix.service.impl.UserServiceImpl;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户控制层
 *
 * @Author: Hor
 * @Date: 2024/5/22 22:40
 * @Version: 1.0
 */
@EnableAsync
@RestController
@ResponseResult
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserServiceImpl userService;

    @Resource
    private UserMapping userMapping;

    @Resource
    private MenuServiceImpl menuService;

    @Resource
    private AuditServiceImpl auditService;


    private static final String CAPTCHA_EXPIRED = "验证码已失效";
    private static final String CAPTCHA_ERROR = "验证码错误";

    /**
     * 登录用户
     *
     * @param loginRequest 登录信息
     * @return 结果
     */
    @PostMapping("login")
    public UserLoginVo login(@RequestBody @Valid UserLoginRequest loginRequest) {
        // 验证码前缀
        String captchaKey = CacheConstants.CAPTCHA_KEY_PREFIX + loginRequest.getUuid();
        // 获取验证码
        String captcha = RedisUtils.get(captchaKey);
        // 验证码已失效
        Assert.notBlank(captcha, CAPTCHA_EXPIRED);
        // 验证成功删除
        RedisUtils.delete(captchaKey);
        // 验证码是否相等
        Assert.equals(loginRequest.getCaptcha(), captcha, CAPTCHA_ERROR);

        Long userId = userService.login(loginRequest);
        return UserLoginVo.builder()
                .id(String.valueOf(userId))
                .token(StpUtil.getTokenValue())
                .build();
    }

    @PostMapping("/email")
    public UserLoginVo emailLogin(@Validated @RequestBody EmailLoginRequest loginReq) {
        String email = loginReq.getEmail();
        String captchaKey = CacheConstants.CAPTCHA_KEY_PREFIX + email;
        String captcha = RedisUtils.get(captchaKey);
        // 验证码已失效
        Assert.notBlank(captcha, CAPTCHA_EXPIRED);
        // 验证码是否相等
        Assert.equals(loginReq.getCaptcha(), captcha, CAPTCHA_ERROR);
        RedisUtils.delete(captchaKey);

        Long userId = userService.emailLogin(email);

        return UserLoginVo.builder()
                .id(String.valueOf(userId))
                .token(StpUtil.getTokenValue()).build();
    }


    /**
     * 注销登录
     *
     * @return 结果
     */
    @PostMapping("logout")
    public Result<?> logout() {
        StpUtil.getSession().delete("user");
        StpUtil.logout();
        return Result.buildSuccess("注销登录成功");
    }


    /**
     * 获取当前用户信息
     *
     * @return UserVo 用户信息
     */
    @SaCheckLogin
    @GetMapping("getUserInfo")
    public UserVo getUserInfo() {
        // 获取当前用户
        User user = userService.selectUserById(StpUtil.getLoginIdAsLong());
        UserVo userVo = userMapping.userToUserVo(user);
        // 填充用户角色
        userVo.setRoles(StpUtil.getRoleList());
        // 填充用户权限
        userVo.setPermission(StpUtil.getPermissionList());
        return userVo;
    }

    /**
     * 获取用户信息
     */
    @PostMapping("getUser")
    public UserVo getUser(@RequestBody @Valid IdRequest idRequest) {
        User user = userService.selectUserById(idRequest.getId());
        List<String> roleNames = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        List<String> roleIds = user.getRoles().stream().map(Role::getCode).collect(Collectors.toList());
        UserVo userVo = userMapping.userToUserVo(user);
        userVo.setRoleNames(roleNames);
        userVo.setRoles(roleIds);
        return userVo;
    }


    /**
     * 修改用户
     */
    @SaCheckLogin
    @PostMapping("edit")
    public Boolean editUser(@RequestBody UserEditRequest editRequest) {
        return userService.updateProfile(editRequest, StpUtil.getLoginIdAsLong());
    }

    /**
     * 修改密码
     *
     * @param updateReq
     */
    @SaCheckLogin
    @PostMapping("/password")
    public void updatePassword(@Validated @RequestBody UserPasswordUpdateRequest updateReq) {
        userService.resetUserPassword(StpUtil.getLoginIdAsLong(), updateReq.getOldPassword(), updateReq.getNewPassword());
        // 修改后登出
        StpUtil.logout();
    }


    /**
     * 修改邮箱
     */
    @SaCheckLogin
    @PostMapping("/updateEmail")
    public void updateUserEmail(@Validated @RequestBody UserEmailUpdateRequest updateReq) {
        String captchaKey = CacheConstants.CAPTCHA_KEY_PREFIX + updateReq.getEmail();
        String captcha = RedisUtils.get(captchaKey);
        // 验证码已失效
        Assert.notBlank(captcha, CAPTCHA_EXPIRED);
        // 验证码是否相等
        Assert.equals(updateReq.getCaptcha(), captcha, CAPTCHA_ERROR);
        RedisUtils.delete(captchaKey);
        userService.updateEmail(updateReq.getEmail(), updateReq.getOldPassword(), StpUtil.getLoginIdAsLong());
    }

    /**
     * 开发者认证提交
     */
    @SaCheckLogin
    @PostMapping("/applyDeveloper")
    public Boolean applyDeveloper(@Validated @RequestBody ApplyDeveloperRequest request) {

        String captchaKey = CacheConstants.CAPTCHA_KEY_PREFIX + request.getEmail();
        String captcha = RedisUtils.get(captchaKey);
        // 验证码已失效
        Assert.notBlank(captcha, CAPTCHA_EXPIRED);
        // 验证码是否相等
        Assert.equals(request.getCaptcha(), captcha, CAPTCHA_ERROR);
        RedisUtils.delete(captchaKey);

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
    @GetMapping("/getDevStatus")
    public Audit getDevStatus() {
        return auditService.selectAuditStatus(StpUtil.getLoginIdAsLong(), 2);
    }




    /**
     * 构建当前用户的菜单路由
     */
    @SaCheckLogin
    @GetMapping("routes")
    public List<Menu> buildMenuListRoutes() {
        // 如果是上帝账户
        if (StpUtil.hasRole("*")) {
            return menuService.selectMenuTreeByAll();
        }
        // 1. 获取当前用户的角色列表
        List<String> roleList = StpUtil.getRoleList();
        return menuService.selectMenuTreeByRoleList(roleList);
    }
}
