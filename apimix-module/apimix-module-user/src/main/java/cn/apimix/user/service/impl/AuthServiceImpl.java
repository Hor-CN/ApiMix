package cn.apimix.user.service.impl;

import cn.apimix.user.LoginHandler;
import cn.apimix.user.LoginHandlerFactory;
import cn.apimix.user.enunms.AuthTypeEnum;
import cn.apimix.user.model.entity.Menu;
import cn.apimix.user.model.mapstruct.MenuMapping;
import cn.apimix.user.model.req.LoginReq;
import cn.apimix.user.model.resp.LoginResp;
import cn.apimix.user.model.resp.RouteResp;
import cn.apimix.user.service.AuthService;
import cn.apimix.user.service.MenuService;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 认证业务实现
 *
 * @Author: Hor
 * @Date: 2025/1/29 13:23
 * @Version: 1.0
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final LoginHandlerFactory loginHandlerFactory;
    private final MenuService menuService;
    private final MenuMapping menuMapping;

    /**
     * 登录
     *
     * @param req     登录请求参数
     * @param request 请求对象
     * @return 登录响应参数
     */
    @Override
    public LoginResp login(LoginReq req, HttpServletRequest request) {
        // 获取登录方式
        AuthTypeEnum authType = req.getAuthType();
        // 获取处理器
        LoginHandler<LoginReq> loginHandler = loginHandlerFactory.getHandler(authType);
        // 登录前置处理
        loginHandler.preLogin(req, request);
        // 登录
        LoginResp loginResp = loginHandler.login(req, request);
        // 登录后置处理
        loginHandler.postLogin(req, request);
        return loginResp;
    }

    /**
     * 构建路由树
     *
     * @param userId 用户 ID
     * @return 路由树
     */
    @Override
    public List<RouteResp> buildRouteTree(Long userId) {
        List<Menu> menus;
        // 如果是上帝账户
        if (StpUtil.hasRole("*")) {
            menus = menuService.selectMenuTreeByAll();
        }else {
            // 获取当前用户的角色列表
            List<String> roleList = StpUtil.getRoleList();
            menus = menuService.selectMenuTreeByRoleList(roleList);
        }
        return menuMapping.menusToRouteResp(menus);
    }
}
