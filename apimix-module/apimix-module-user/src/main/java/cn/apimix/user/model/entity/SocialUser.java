package cn.apimix.user.model.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  实体类。
 *
 * @author Hor
 * @since 2024-10-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("social_user")
public class SocialUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * GITHUB、GITEE、QQ，更多请参考
     */
    private String source;

    /**
     * 开放ID
     */
    private String openId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 附加信息
     */
    private String metaJson;


    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
