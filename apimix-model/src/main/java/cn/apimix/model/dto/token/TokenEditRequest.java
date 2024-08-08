package cn.apimix.model.dto.token;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 修改 Token 请求
 *
 * @Author: Hor
 * @Date: 2024/5/20 17:41
 * @Version: 1.0
 */
@Data
public class TokenEditRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 过期时间
     */
    private LocalDateTime expired;

    /**
     * remark 备注
     */
    private String remark;

}
