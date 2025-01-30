package cn.apimix.model.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户账户表
 *
 * @Author: Hor
 * @Date: 2024/6/24 上午10:47
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "user_account")
public class UserAccount implements Serializable {


    @Id(keyType = KeyType.Auto)
    private Long id;

    private Long userId;

    private Boolean status;

    private Long amount;

    private Long amountTotal;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
