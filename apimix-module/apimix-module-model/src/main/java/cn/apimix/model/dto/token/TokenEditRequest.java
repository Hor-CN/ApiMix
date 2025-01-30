package cn.apimix.model.dto.token;

import com.fasterxml.jackson.annotation.JsonFormat;
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
     * 名称
     */
    private String name;

    /**
     * 状态
     */
    private Boolean status;


    private Boolean isExpired;


    /**
     * remark 备注
     */
    private String remark;

    /**
     * 过期时间
     */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expired;


}
