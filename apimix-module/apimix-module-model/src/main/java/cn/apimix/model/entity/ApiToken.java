package cn.apimix.model.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: Hor
 * @Date: 2024/8/8 下午9:31
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "api_token")
public class ApiToken implements Serializable {

    /**
     * 主键
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 接口 id
     */
    private Long apiId;

    /**
     * tokenId
     */
    private Long tokenId;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 分配额度
     */
    private Long totalQuota;

    /**
     * 已用额度
     */
    private Long usedQuota;

    /**
     * 是否不受限制的 Token
     */
    private Boolean isUnlimited;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
