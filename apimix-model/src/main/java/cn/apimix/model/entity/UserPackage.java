package cn.apimix.model.entity;

import com.mybatisflex.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体类。
 *
 * @author Hor
 * @since 2024-06-17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("user_package")
public class UserPackage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 套餐ID
     */
    private Long packageId;

    /**
     * apiID
     */
    private Long apiId;

    /**
     * 此套餐总次数
     */
    private Long totalQuota;

    /**
     * 当前套餐已使用次数
     */
    private Long usedQuota;

    /**
     * 过期时间戳
     */
    private Date expiredTime;

    /**
     * 购买数量
     */
    private Integer count;

    /**
     * 套餐状态（0：未使用 1：使用中，2：已用完，3：过期）
     */
    @Column(ignore = true)
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @RelationOneToOne(selfField = "packageId", targetField = "id")
    private Package packageInfo;

}
