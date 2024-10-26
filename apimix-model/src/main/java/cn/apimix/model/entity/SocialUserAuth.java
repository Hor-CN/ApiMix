package cn.apimix.model.entity;

import com.mybatisflex.annotation.Id;
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
@Table("social_user_auth")
public class SocialUserAuth implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 系统用户id
     */
    private Long userId;

    /**
     * 社会化用户ID
     */
    private Integer socialUserId;

}
