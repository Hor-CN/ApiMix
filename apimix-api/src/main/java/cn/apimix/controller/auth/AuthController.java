package cn.apimix.controller.auth;

import cn.apimix.auth.config.WxGzhConfig;
import cn.apimix.auth.handler.wx.WxChatMsgFactory;
import cn.apimix.auth.handler.wx.WxChatMsgHandler;
import cn.apimix.auth.model.req.LoginReq;
import cn.apimix.auth.model.resp.LoginResp;
import cn.apimix.auth.model.resp.SocialAuthAuthorizeResp;
import cn.apimix.auth.service.AuthService;
import cn.apimix.auth.utils.MessageUtil;
import cn.apimix.auth.utils.SHA1;
import cn.apimix.common.resp.Result;
import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.core.exception.HorApiException;
import cn.apimix.model.entity.Role;
import cn.apimix.model.entity.User;
import cn.apimix.model.mapstruct.UserMapping;
import cn.apimix.model.vo.RouteResp;
import cn.apimix.model.vo.user.UserInfoResp;
import cn.apimix.service.UserService;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: Hor
 * @Date: 2024/12/15 23:33
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@ResponseResult
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    private final UserMapping userMapping;

    private final AuthService authService;

    private final AuthRequestFactory authRequestFactory;

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
        return SocialAuthAuthorizeResp.builder()
                .authorizeUrl(authRequest.authorize(AuthStateUtils.createState()))
                .build();
    }


    @Resource
    private WxGzhConfig wxGzhConfig;

    @Resource
    private WxChatMsgFactory wxChatMsgFactory;


    /**
     * 回调消息校验
     */
    @GetMapping("wxCallback")
    public String callback(@RequestParam("signature") String signature,
                           @RequestParam("timestamp") String timestamp,
                           @RequestParam("nonce") String nonce,
                           @RequestParam("echostr") String echostr) {
        log.info("get验签请求参数：signature:{}，timestamp:{}，nonce:{}，echostr:{}",
                signature, timestamp, nonce, echostr);
        String shaStr = SHA1.getSHA1(wxGzhConfig.getAppToken(), timestamp, nonce, "");
        if (signature.equals(shaStr)) {
            return echostr;
        }
        return "unknown";
    }

    @PostMapping(value = "wxCallback", produces = "application/xml;charset=UTF-8")
    public String callback(
            @RequestBody String requestBody,
            @RequestParam("signature") String signature,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestParam(value = "msg_signature", required = false) String msgSignature) {
        log.info("接收到微信消息：requestBody：{}", requestBody);
        Map<String, String> messageMap = MessageUtil.parseXml(requestBody);
        String msgType = messageMap.get("MsgType");
        String event = messageMap.get("Event") == null ? "" : messageMap.get("Event");
        log.info("msgType:{},event:{}", msgType, event);

        StringBuilder sb = new StringBuilder();
        sb.append(msgType);
        if (!StringUtils.isEmpty(event)) {
            sb.append(".");
            sb.append(event);
        }
        String msgTypeKey = sb.toString();
        WxChatMsgHandler wxChatMsgHandler = wxChatMsgFactory.getHandlerByMsgType(msgTypeKey);
        if (Objects.isNull(wxChatMsgHandler)) {
            return "unknown";
        }
        String replyContent = wxChatMsgHandler.dealMsg(messageMap);
        log.info("replyContent:{}", replyContent);
        return replyContent;
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
