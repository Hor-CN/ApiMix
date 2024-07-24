package cn.apimix.model.vo.user;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: Hor
 * @Date: 2024/5/22 22:43
 * @Version: 1.0
 */
@Data
@Builder
public class UserLoginVo {

    /**
     * 用户ID
     */
    private String id;

    /**
     * token
     */
    private String token;
}
