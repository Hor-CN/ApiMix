package cn.apimix.controller;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.core.core.model.IdRequest;
import cn.apimix.common.resp.Result;
import cn.apimix.model.dto.user.UserEditRequest;
import cn.apimix.model.dto.user.UserLoginRequest;
import cn.apimix.model.entity.Menu;
import cn.apimix.model.entity.Role;
import cn.apimix.model.entity.User;
import cn.apimix.model.mapstruct.UserMapping;
import cn.apimix.model.vo.user.UserLoginVo;
import cn.apimix.model.vo.user.UserVo;
import cn.apimix.service.impl.EmailServiceImpl;
import cn.apimix.service.impl.MenuServiceImpl;
import cn.apimix.service.impl.UserServiceImpl;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
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
    private EmailServiceImpl emailService;


    /**
     * 登录用户
     *
     * @param loginRequest 登录信息
     * @return 结果
     */
    @PostMapping("login")
    public UserLoginVo login(@RequestBody @Valid UserLoginRequest loginRequest) {
        Long userId = userService.login(loginRequest);
        return UserLoginVo.builder()
                .id(String.valueOf(userId))
                .token(StpUtil.getTokenValue())
                .build();
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

    @Async("threadPool")
    @RequestMapping("/emailCode")
    public void getEmailCaptcha(@RequestParam String email) {
        emailService.sendEmailCode(email);
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
