package cn.apimix.auth;

import cn.apimix.auth.enunms.AuthTypeEnum;
import cn.apimix.auth.model.req.LoginReq;
import cn.apimix.auth.model.resp.ClientResp;
import cn.apimix.auth.model.resp.LoginResp;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录处理器
 *
 * @Author: Hor
 * @Date: 2025/1/29 11:25
 * @Version: 1.0
 */
public interface LoginHandler<T extends LoginReq> {

    /**
     * 登录
     *
     * @param req     登录请求参数
     * @param request 请求对象
     * @return 登录响应参数
     */
    LoginResp login(T req, HttpServletRequest request);

    /**
     * 登录前置处理
     *
     * @param req     登录请求参数
     * @param request 请求对象
     */
    void preLogin(T req, HttpServletRequest request);

    /**
     * 登录后置处理
     *
     * @param req     登录请求参数
     * @param request 请求对象
     */
    void postLogin(T req, HttpServletRequest request);

    /**
     * 获取认证类型
     *.
     * @return 认证类型
     */
    AuthTypeEnum getAuthType();
}