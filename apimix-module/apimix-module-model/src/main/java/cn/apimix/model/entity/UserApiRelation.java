package cn.apimix.model.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.RelationOneToOne;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户接口关联实体
 *
 * @Author: Hor
 * @Date: 2024/5/20 19:52
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "user_api_relation")
public class UserApiRelation implements Serializable {

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
     * 接口 id
     */
    private Long apiId;

    /**
     * 1-正常，0-禁用
     */
    private Boolean status;

    /**
     * 创建时间（申请日期）
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
