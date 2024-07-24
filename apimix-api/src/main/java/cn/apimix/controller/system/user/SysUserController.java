package cn.apimix.controller.system.user;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.model.dto.system.user.*;
import cn.apimix.model.entity.User;
import cn.apimix.model.mapstruct.UserMapping;
import cn.apimix.model.vo.user.UserVo;
import cn.apimix.service.impl.MenuServiceImpl;
import cn.apimix.service.impl.RoleServiceImpl;
import cn.apimix.service.impl.UserServiceImpl;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Hor
 * @Date: 2024/5/24 21:26
 * @Version: 1.0
 */
@RestController
@ResponseResult
@RequestMapping("/api/system/user")
public class SysUserController {


    @Resource
    private UserServiceImpl userService;

    @Resource
    private RoleServiceImpl roleService;

    @Resource
    private MenuServiceImpl menuService;

    @Resource
    private UserMapping userMapping;

    /**
     * 用户分页查询
     *
     * @param queryRequest 分页查询对象
     * @return 分页对象
     */
    @SaCheckLogin // 登录检验
    @SaCheckPermission("sys:user:list") // 权限检验
    @GetMapping()
    public Page<UserVo> pageList(SysUserQueryRequest queryRequest) {
        // 获取用户数据
        Page<User> userPage = userService.selectUserByPage(queryRequest);
        // 转化VO
        List<UserVo> userVos = userMapping.usersToUserVos(userPage.getRecords());
        // 返回结果集
        return new Page<>(userVos, userPage.getPageNumber(), userPage.getPageSize(), userPage.getTotalPage());
    }


    /**
     * 新增用户
     */
    @SaCheckLogin
    @SaCheckPermission("sys:user:add")
    @PostMapping("save")
    public Boolean saveSystemUser(@RequestBody @Valid SysUserAddRequest addRequest) {
        return userService.insertUser(addRequest);
    }

    /**
     * 修改用户
     */
    @SaCheckLogin
    @SaCheckPermission("sys:user:edit")
    @PostMapping("edit")
    public Boolean editSystemUser(@RequestBody SysUserEditRequest editRequest) {
        // 如果要分配的角色是上帝,但修改的不是自己
        if (editRequest.getRoles().contains("*")) {
            Assert.isFalse(StpUtil.hasRole("*") && editRequest.getId() != StpUtil.getLoginIdAsLong(),
                    "无法修改");
        }

        // 如果当前用户本身是上帝,权限不能取消
        if (StpUtil.hasRole("*") && editRequest.getId() == StpUtil.getLoginIdAsLong()) {
            editRequest.setRoles(Collections.singletonList("*"));
        }
        return userService.updateUser(editRequest);
    }

    /**
     * 修改密码
     */
    @SaCheckLogin
    @SaCheckPermission("sys:user:resetPwd")
    @PostMapping("resetPwd")
    public Boolean resetPassword(@RequestBody SysUserRetPwdRequest retPwdRequest) {
        // 获取要修改的用户ID
        Long userId = Long.valueOf(retPwdRequest.getId());
        // 如果要修改的用户是管理员
        if (StpUtil.hasRole(userId, "admin") && !StpUtil.hasRole("*")) {
            // 判断是否为自己
            Assert.isTrue(StpUtil.getLoginIdAsLong() == userId, "无法修改");
        }
        // 如果要修改的是上帝，判断自己是否是上帝
        if (StpUtil.hasRole(userId, "*")) {
            Assert.isTrue(StpUtil.hasRole("*"), "无法修改");
        }
        // 修改密码
        Boolean isReset = userService.sysResetUserPassword(userId, retPwdRequest.getPassword());
        // 修改成功时,如果该用户已经登录将已登录的用户注销,让其重新登录
        if (isReset && StpUtil.isLogin(userId)) {
            StpUtil.logout(retPwdRequest.getId());
        }
        return isReset;
    }


    /**
     * 根据主键获取用户详细信息。
     */
    @GetMapping("detail")
    public UserVo getInfo(
            @RequestParam
            @NotBlank(message = "ID不能为空")
            @Pattern(regexp = "\\d+", message = "ID 必须是数字")
            String id
    ) {
        Long userId = Long.valueOf(id);
        UserVo userVo = userMapping.userToUserVo(userService.selectUserById(userId));
        // 获取该用户的分配的角色code
        List<String> roles = roleService.selectRoleCodeByUserId(userId);
        userVo.setRoles(roles);
        // 获取角色名称
        List<String> roleNames = roleService.selectRoleNameByRoleCode(roles);
        userVo.setRoleNames(roleNames);
        // 获取该用户的角色所拥有的权限
        userVo.setPermission(menuService.selectPermsByRoleCodes(roles));
        return userVo;
    }

    /**
     * 删除用户
     */
    @SaCheckLogin
    @SaCheckPermission("sys:user:del")
    @PostMapping("delete")
    public Boolean delUser(@RequestBody @Valid SysUserDelRequest delRequest) {
        List<Long> userIds = delRequest.getIds().stream().map(Long::valueOf).collect(Collectors.toList());
        for (Long userId : userIds) {
            // 如果要删除的用户是管理员需要判断是否是自己
            if (StpUtil.hasRole(userId, "admin")) {
                Assert.isTrue(StpUtil.getLoginIdAsLong() == userId, "无法删除");
            }
            // 如果要修改的是上帝，判断自己是否是上帝
            if (StpUtil.hasRole(userId, "*")) {
                Assert.isTrue(StpUtil.hasRole("*"), "无法删除");
            }
        }
        return userService.deleteUserByIds(userIds);

    }

}
