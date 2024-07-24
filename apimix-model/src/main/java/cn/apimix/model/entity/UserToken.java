package cn.apimix.model.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户Token实体
 *
 * @Author: Hor
 * @Date: 2024/5/20 19:43
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "user_token")
public class UserToken implements Serializable {

    /**
     * 主键
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * Token 值
     */
    private String tokenValue;
    
    /**
     * remark 备注
     */
    private String remark;

    /**
     * 过期时间
     */
    private Long expired;

    /**
     * 创建时间（申请日期）
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
