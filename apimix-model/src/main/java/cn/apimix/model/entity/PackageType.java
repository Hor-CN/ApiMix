package cn.apimix.model.entity;

import com.mybatisflex.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  实体类。
 *
 * @author Hor
 * @since 2024-06-17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("package_type")
public class PackageType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 套餐类型名称
     */
    private String name;

    /**
     * 逻辑删除标志（0代表存在 1代表删除）
     */
    @Column(isLogicDelete = true)
    private Boolean isDelete;


//    @RelationOneToMany(selfField = "id", targetField = "packageType")
//    private List<Package> list;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
