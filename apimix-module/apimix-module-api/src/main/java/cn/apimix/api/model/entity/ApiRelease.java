package cn.apimix.api.model.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *  实体类。
 *
 * @author Hor
 * @since 2025-02-17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("api_release")
public class ApiRelease implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
    private Long id;

    /**
     * 接口ID
     */
    private Long currentVersionId;

    /**
     * 状态：0-下线，1-上线
     */
    private Boolean status;

    /**
     * 创建人
     */
    private Long userId;

    /**
     * 逻辑删除标志（0存在，1删除）
     */
    @Column(isLogicDelete = true)
    private Boolean isDelete;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
