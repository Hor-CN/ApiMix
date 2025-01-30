package cn.apimix.auth.handler;

import cn.apimix.auth.AbstractLoginHandler;
import cn.apimix.auth.enunms.AuthTypeEnum;
import cn.apimix.auth.model.req.EmailLoginReq;
import cn.apimix.auth.model.resp.LoginResp;
import cn.apimix.model.entity.User;
import cn.apimix.model.entity.table.UserTableDef;
import cn.hutool.core.lang.Assert;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 邮箱登录处理器
 *
 * @Author: Hor
 * @Date: 2025/1/29 15:03
 * @Version: 1.0
 */
@Component
public class EmailLoginHandler extends AbstractLoginHandler<EmailLoginReq> {
    /**
     * 登录
     *
     * @param req     登录请求参数
     * @param request 请求对象
     * @return 登录响应参数
     */
    @Override
    public LoginResp login(EmailLoginReq req, HttpServletRequest request) {
        // 验证邮箱
        boolean exists = userService.queryChain().where(UserTableDef.USER.EMAIL.eq(req.getEmail())).exists();
        Assert.isTrue(exists, "此邮箱未绑定本系统账号");
        User user = userService.queryChain().where(UserTableDef.USER.EMAIL.eq(req.getEmail())).one();
        // 检查用户状态
        super.checkUserStatus(user);
        // 执行认证
        String token = this.authenticate(user);
        return LoginResp.builder()
                .id(user.getId())
                .token(token).build();
    }


    /**
     * 登录前置处理
     *
     * @param req     登录请求参数
     * @param request 请求对象
     */
    @Override
    public void preLogin(EmailLoginReq req, HttpServletRequest request) {
        super.preLogin(req, request);
        String email = req.getEmail();
        captchaService.checkEmailCode(email, req.getCaptcha());
    }

    /**
     * 获取认证类型
     * .
     *
     * @return 认证类型
     */
    @Override
    public AuthTypeEnum getAuthType() {
        return AuthTypeEnum.EMAIL;
    }
}
