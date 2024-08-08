package cn.apimix.common.service;

import cn.apimix.common.model.InterfaceInfo;
import cn.apimix.common.model.InterfaceToken;
import cn.apimix.common.model.InterfaceUser;

/**
 * @Author: Hor
 * @Date: 2024/6/16 下午7:54
 * @Version: 1.0
 */
public interface InnerInterfaceService {


    /**
     * 接口调用
     * 按次数扣
     * todo 不按流量包，可能需要根据接口每次单价来扣费
     *
     * @param apiId 接口ID
     * @param token 调用此接口的Token
     * @return boolean 扣费结果
     */
    Boolean invoke(Long apiId, String token);


    /**
     * 获取调用用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    InterfaceUser getUserByUserId(Long userId);


    /**
     * 获取Token信息
     *
     * @param tokenValue token值
     * @return token信息
     */
    InterfaceToken getTokenByTokenValue(String tokenValue);

    /**
     * 获取接口信息
     *
     * @param apiId 接口id
     * @return 接口信息
     */
    InterfaceInfo getInterfaceInfo(Long apiId);

}
