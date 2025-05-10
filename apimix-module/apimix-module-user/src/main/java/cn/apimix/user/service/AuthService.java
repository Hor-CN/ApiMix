package cn.apimix.user.service;

import cn.apimix.user.model.req.LoginReq;
import cn.apimix.user.model.resp.LoginResp;
import cn.apimix.user.model.resp.RouteResp;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 认证业务接口
 *
 * @Author: Hor
 * @Date: 2025/1/29 13:21
 * @Version: 1.0
 */
public interface AuthService {
    /**
     * 登录
     *
     * @param req     登录请求参数
     * @param request 请求对象
     * @return 登录响应参数
     */
    LoginResp login(LoginReq req, HttpServletRequest request);

    /**
     * 构建路由树
     *
     * @param userId 用户 ID
     * @return 路由树
     */
    List<RouteResp> buildRouteTree(Long userId);
}
