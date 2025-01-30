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
@Table("package")
public class Package implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 套餐的费用
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * ApiID
     */
    private Long apiId;

    /**
     * 套餐类型
     */
    private Integer packageType;

    /**
     * 请求次数
     */
    private Long quota;

    /**
     * 套餐的费用
     */
    private Long price;


    /**
     * 限购次数（0：无限制 >0：有次数限制）
     */
    private Integer limitQuota;

    /**
     * 套餐过期天数
     */
    private Integer expired;

    /**
     * 逻辑删除标志（0代表存在 1代表删除）
     */
    @Column(isLogicDelete = true)
    private Boolean isDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @RelationOneToOne(selfField = "packageType", targetField = "id")
    private PackageType type;

}
