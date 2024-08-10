package cn.apimix.provider;

import cn.apimix.common.model.InterfaceInfo;
import cn.apimix.common.model.InterfaceLog;
import cn.apimix.common.model.InterfaceToken;
import cn.apimix.common.model.InterfaceUser;
import cn.apimix.common.service.InnerInterfaceService;
import cn.apimix.core.exception.HorApiException;
import cn.apimix.model.entity.*;
import cn.apimix.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/6/16 下午7:58
 * @Version: 1.0
 */
@Slf4j
@Component
@DubboService
public class InnerInterfaceServiceImpl implements InnerInterfaceService {

    @Resource
    private ApiServiceImpl apiService;

    @Resource
    private UserTokenServiceImpl tokenService;

    @Resource
    private UserServiceImpl userService;

    @Resource
    private UserApiRelationServiceImpl relationService;

    @Resource
    private UserPackageServiceImpl userPackageService;

    @Resource
    private ApiTokenServiceImpl apiTokenService;

    @Resource
    private ApiLogServiceImpl apiLogService;

    /**
     * 接口调用
     * 按次数扣
     * todo 不按流量包，可能需要根据接口每次单价来扣费
     *
     * @param apiId 接口ID
     * @param token 调用此接口的Token
     * @return boolean 扣费结果
     */
    @Transactional
    @Override
    public Boolean invoke(Long apiId, String token, InterfaceLog interfaceLog) {

        try {
            ApiInfo apiInfo = apiService.getById(apiId);
            // 如果不收费，直接调用成功
            if (!apiInfo.getIsPaid()) {
                return true;
            }

            // 根据 Token 获取信息
            UserToken userToken = tokenService.selectTokenByTokenValue(token);

            // 根据条件使用最适合的流量包【0】
            List<UserPackage> availablePackages = userPackageService.getAvailablePackages(apiId, userToken.getUserId());
            // 添加套餐的使用次数
            Boolean packageIncrease = userPackageService.increaseTheNumberOfCalls(availablePackages.get(0).getId());

            ApiToken apiToken = apiTokenService.getApiTokenByTokenIdAndApiId(userToken.getId(), apiId);
            // 分配的token统计增加
            Boolean apiTokenIncrease = true;
            if (apiToken != null) {
                apiTokenIncrease = apiTokenService.increaseTheNumberOfCalls(apiToken.getTokenId());
            }

            // 添加日志
            Boolean logIncrease = apiLogService.insertApiLog(interfaceLog);
            return packageIncrease && apiTokenIncrease && logIncrease;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }

    }

    /**
     * 获取调用用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    @Override
    public InterfaceUser getUserByUserId(Long userId) {
        User user = userService.selectUserById(userId);
        return InterfaceUser.builder()
                .id(user.getId())
                .avatar(user.getAvatar())
                .username(user.getUserName())
                .nickname(user.getNickName())
                .status(user.getStatus())
                .createTime(user.getCreateTime())
                .updateTime(user.getUpdateTime())
                .build();
    }

    /**
     * 获取Token信息
     *
     * @param tokenValue token值
     * @return token信息
     */
    @Override
    public InterfaceToken getTokenByTokenValue(String tokenValue) {
        UserToken userToken = tokenService.selectTokenByTokenValue(tokenValue);
        return InterfaceToken.builder()
                .id(userToken.getUserId())
                .userId(userToken.getUserId())
                .tokenValue(userToken.getTokenValue())
                .expired(userToken.getExpired())
                .remark(userToken.getRemark())
                .createTime(userToken.getCreateTime())
                .updateTime(userToken.getUpdateTime())
                .build();
    }

    /**
     * 获取接口信息
     *
     * @param apiId 接口id
     * @return 接口信息
     */
    @Override
    public InterfaceInfo getInterfaceInfo(Long apiId) {
        ApiInfo apiInfo = apiService.getById(apiId);

        return InterfaceInfo.builder()
                .id(apiInfo.getId())
                .userId(apiInfo.getUserId())
                .name(apiInfo.getName())
                .url(apiInfo.getUrl())
                .proxy(apiInfo.getProxy())
                .isPaid(apiInfo.getIsPaid())
                .method(apiInfo.getMethod())
                .status(apiInfo.getStatus())
                .createTime(apiInfo.getCreateTime())
                .updateTime(apiInfo.getUpdateTime())
                .build();
    }

    /**
     * 是否能调用
     *
     * @param apiId 接口ID
     * @param token token
     * @return boolean
     */
    @Override
    public Boolean isInvoke(Long apiId, String token) {
        // 根据 Token 获取信息
        UserToken userToken = tokenService.selectTokenByTokenValue(token);
        // 根据 接口ID 获取信息
        ApiInfo apiInfo = apiService.getById(apiId);

        // 未代理接口无法调用
        if (!apiInfo.getProxy()) {
            return false;
        }

        // 如果不收费
        if (!apiInfo.getIsPaid()) {
            return true;
        }

        // 判断接口是否被用户申请
        Long userId = userToken.getUserId();
        Boolean userApiRelationExist = relationService.isUserApiRelationExist(apiId, userId);
        // 尚未购买该 API
        if (!userApiRelationExist) {
            return false;
        }

        // 1. 判断Token是否有分配到此接口，有则判断此token在此接口上的限制次数
        ApiToken apiToken = apiTokenService.getApiTokenByTokenIdAndApiId(userToken.getId(), apiId);
        // 未分配，没有可用次数返回false
        if (apiToken != null && apiToken.getTotalQuota() < apiToken.getUsedQuota()) {
            return false;
        }

        UserApiRelation userApiRelation = relationService.selectApiRelation(apiId, userId);
        // 禁用调用
        if (!userApiRelation.getStatus()) {
            return false;
        }

        // 获取可用套餐
        List<UserPackage> availablePackages = userPackageService.getAvailablePackages(apiId, userId);

        return !availablePackages.isEmpty();

    }


}
