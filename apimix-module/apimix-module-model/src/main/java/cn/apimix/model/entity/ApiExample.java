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
 * API示例实体
 *
 * @Author: Hor
 * @Date: 2024/5/20 19:52
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "api_example")
public class ApiExample implements Serializable {

    /**
     * 主键ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 接口ID
     */
    private Long apiId;

    /**
     * 示例名称
     */
    private String name;

    /**
     * 状态码
     */
    private Long code;

    /**
     * 返回类型
     * application/json
     */
    private String type;

    /**
     * 示例内容
     */
    private String content;

}