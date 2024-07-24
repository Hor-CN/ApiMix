package cn.apimix.model.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  实体类。
 *
 * @author Hor
 * @since 2024-05-31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *  主键
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序值
     */
    private Integer order;

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

}
