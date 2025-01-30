package cn.apimix.model.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 实体类。
 *
 * @author Hor
 * @since 2024-05-31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("category_api")
public class CategoryApi implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 接口ID
     */
    private Long apiId;

}
