package cn.apimix.model.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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
     * tokenId
     */
    private Long tokenId;

    /**
     * 接口 id
     */
    private Long apiId;

    /**
     * 总调用次数(总额度，包括所有的套餐)
     */
    private Long totalQuota;

    /**
     * 已调用次数(已用额度)
     */
    private Long usedQuota;

    /**
     * 是否不受限制的 Token
     */
    private Boolean isUnlimited;

    /**
     * 创建时间
     */
    private Date createTime;

}
