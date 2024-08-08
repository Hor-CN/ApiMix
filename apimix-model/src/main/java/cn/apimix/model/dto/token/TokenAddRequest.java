package cn.apimix.model.dto.token;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Token 添加请求
 *
 * @Author: Hor
 * @Date: 2024/5/20 17:38
 * @Version: 1.0
 */
@Data
public class TokenAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 过期时间（时间戳）
     */
    private LocalDateTime expired;

    /**
     * remark 备注
     */
    private String remark = "暂无备注";
}
