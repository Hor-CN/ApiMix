package cn.apimix.provider;

import cn.apimix.common.service.IUserInterfaceInvokeService;
import cn.apimix.model.entity.ApiInfo;
import cn.apimix.model.entity.UserApiRelation;
import cn.apimix.model.entity.UserToken;
import cn.apimix.service.impl.ApiServiceImpl;
import cn.apimix.service.impl.UserApiRelationServiceImpl;
import cn.apimix.service.impl.UserTokenServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: Hor
 * @Date: 2024/6/16 下午7:58
 * @Version: 1.0
 */
@Component
@DubboService
public class UserInterfaceInvokeServiceImpl implements IUserInterfaceInvokeService {

    @Resource
    private ApiServiceImpl apiService;

    @Resource
    private UserTokenServiceImpl tokenService;


    @Resource
    private UserApiRelationServiceImpl relationService;


//    private

    /**
     * 接口调用
     * 按次数扣
     * todo 不按流量包，可能需要根据接口每次单价来扣费
     *
     * @param apiId 接口ID
     * @param token 调用此接口的Token
     * @return boolean 扣费结果
     */
    @Override
    public Boolean invoke(Long apiId, String token) {
        /* 判断此token是否分配到此api上
            有则附带扣除分配的次数
                判断是否收限制
            无则直接去扣除总调用次数
         */
        ApiInfo apiInfo = apiService.getById(apiId);

        if (!apiInfo.getProxy()) {
            return false;
        }

        // todo 如果接口不收费
        if (!apiInfo.getIsPaid()) {
            return false;
        }


        // 根据 Token 获取信息
        UserToken userToken = tokenService.selectTokenByTokenValue(token);
        // Token 过期判断

        // 判断接口是否被用户申请
        Long userId = userToken.getUserId();

        Boolean userApiRelationExist = relationService.isUserApiRelationExist(apiId, userId);
        // 尚未购买该 API 或 API 调用次数已用完
        if (!userApiRelationExist) {
            return false;
        }
        UserApiRelation userApiRelation = relationService.selectApiRelation(apiId, userId);
        // 禁用调用
        if (!userApiRelation.getStatus()) {
            return false;
        }
        // 没有调用次数了
        if (userApiRelation.getTotalQuota() <= userApiRelation.getUsedQuota()) {
            return false;
        }


        // todo 判断token是否有分配到该接口

        // 使用流量包
        // 1. 根据过期时间使用最短的流量包
        // 2. 使用添加used_quota次数并将用户调用情况表的次数加一

        // 获取最先过期的流量包


        return true;
    }
}
