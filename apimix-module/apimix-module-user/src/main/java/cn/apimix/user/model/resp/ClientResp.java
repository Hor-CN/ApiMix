package cn.apimix.user.model.resp;

import lombok.Data;

import java.util.List;

/**
 * 客户端信息
 *
 * @Author: Hor
 * @Date: 2025/1/29 11:40
 * @Version: 1.0
 */
@Data
public class ClientResp {

    /**
     * 客户端 ID
     */
    private String clientId;

    /**
     * 客户端 Key
     */
    private String clientKey;

    /**
     * 客户端秘钥
     */
    private String clientSecret;

    /**
     * 认证类型
     */
    private List<String> authType;

    /**
     * 客户端类型
     */
    private String clientType;

    /**
     * Token 最低活跃频率（单位：秒，-1：不限制，永不冻结）
     */
    private Long activeTimeout;

    /**
     * Token 有效期（单位：秒，-1：永不过期）
     */
    private Long timeout;

}
