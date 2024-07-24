package cn.apimix.config;

import cn.apimix.service.impl.MenuServiceImpl;
import cn.apimix.service.impl.RoleServiceImpl;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/20 22:30
 * @Version: 1.0
 */
@Slf4j
@Component
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private RoleServiceImpl roleService;

    @Resource
    private MenuServiceImpl menuService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 权限码集合
        List<String> permissionList = new ArrayList<>();
        // 如果是上帝账户
        if (StpUtil.hasRole("*")) {
            permissionList.add("*");
            return permissionList;
        }
        permissionList.addAll(menuService.selectPermsByRoleCodes(getRoleList(loginId, loginType)));
        return permissionList;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return roleService.selectRoleCodeByUserId(Long.parseLong(loginId.toString()));

    }
}
