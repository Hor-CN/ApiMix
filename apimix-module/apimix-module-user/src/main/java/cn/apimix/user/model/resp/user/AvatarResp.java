package cn.apimix.user.model.resp.user;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Hor
 * @Date: 2025/2/16 00:38
 * @Version: 1.0
 */
@Data
@Builder
public class AvatarResp implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 头像地址
     */
    private String avatar;
}