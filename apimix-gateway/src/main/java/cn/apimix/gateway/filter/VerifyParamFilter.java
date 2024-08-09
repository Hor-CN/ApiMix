package cn.apimix.gateway.filter;

import cn.apimix.common.model.InterfaceInfo;
import cn.apimix.common.model.InterfaceToken;
import cn.apimix.common.model.InterfaceUser;
import cn.apimix.common.service.InnerInterfaceService;
import cn.apimix.gateway.exception.BusinessException;
import cn.apimix.gateway.utils.NetUtils;
import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.ZoneOffset;

/**
 * 验证参数
 *
 * @Author: Hor
 * @Date: 2024/6/15 21:28
 * @Version: 1.0
 */
@Slf4j
@Component
public class VerifyParamFilter implements Ordered, GlobalFilter {


    private final static String TOKEN = "X-APIMix-Token";


    @DubboReference
    private InnerInterfaceService innerInterfaceService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 请求信息
        ServerHttpRequest request = exchange.getRequest();
        // 请求头
        HttpHeaders headers = request.getHeaders();
        // 获取Token信息
        String token = headers.getFirst(TOKEN);
        // 请求头中参数必须完整
        if (token == null) {
            throw new BusinessException(403, "Token不能为空");
        }

        // 请求的接口ID
        Long apiId = NetUtils.convertApiId(request);

        InterfaceToken interfaceToken = innerInterfaceService.getTokenByTokenValue(token);
        if (interfaceToken == null) {
            throw new BusinessException(401, "请正确配置接口Token");
        }

        if (interfaceToken.getExpired() != null) {
            if (interfaceToken.getExpired().toEpochSecond(ZoneOffset.of("+8")) <= LocalDateTimeUtil.now().toEpochSecond(ZoneOffset.of("+8"))) {
                throw new BusinessException(403, "Token已过期");
            }
        }

        InterfaceUser interfaceUser = innerInterfaceService.getUserByUserId(interfaceToken.getUserId());
        if (interfaceUser == null) {
            throw new BusinessException(401, "请正确配置接口Token");
        }

        if (interfaceUser.getStatus().equals(0)) {
            throw new BusinessException(400, "该账号已封禁");
        }

        InterfaceInfo interfaceInfo = innerInterfaceService.getInterfaceInfo(apiId);
        if (interfaceInfo == null) {
            throw new BusinessException(404, "接口不存在");
        }
        if (!interfaceInfo.getStatus()) {
            throw new BusinessException(400, "接口未开启");
        }
        if (!interfaceInfo.getProxy() || !interfaceInfo.getIsPaid()) {
            throw new BusinessException(400, "免费或未代理接口");
        }

        if (!innerInterfaceService.isInvoke(apiId, token)) {
            throw new BusinessException(400, "请求超过次数限制");
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
