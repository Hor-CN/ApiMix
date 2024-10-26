package cn.apimix.model.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;

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
    private Integer id;

    /**
     * 第三方系统的唯一ID	
     */
    private String uuid;

    /**
     * GITHUB、GITEE、QQ，更多请参考
     */
    private String source;

    /**
     * 用户的授权令牌
     */
    private String accessToken;

    /**
     * 第三方用户的授权令牌的有效期(部分平台可能没有)
     */
    private Integer expireIn;

    /**
     * 刷新令牌(部分平台可能没有)
     */
    private String refreshToken;

    /**
     * 第三方用户的 open id(部分平台可能没有)
     */
    private String openId;

    /**
     * 第三方用户的 ID(部分平台可能没有)
     */
    private String uid;

    /**
     * 个别平台的授权信息	(部分平台可能没有)
     */
    private String accessCode;

    /**
     * 第三方用户的 union id(部分平台可能没有)
     */
    private String unionId;

    /**
     * 第三方用户授予的权限(部分平台可能没有)
     */
    private String scope;

    /**
     * 个别平台的授权信息(部分平台可能没有)
     */
    private String tokenType;

    /**
     * id token(部分平台可能没有)
     */
    private String idToken;

    /**
     * 用户的授权code(部分平台可能没有)
     */
    private String code;

}
