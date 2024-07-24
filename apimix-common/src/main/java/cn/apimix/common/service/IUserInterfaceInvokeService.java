package cn.apimix.common.service;

/**
 * @Author: Hor
 * @Date: 2024/6/16 下午7:54
 * @Version: 1.0
 */
public interface IUserInterfaceInvokeService {


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


}
