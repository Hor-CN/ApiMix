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
 * 审核表
 *
 * @Author: Hor
 * @Date: 2024/5/27 21:33
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("audit")
public class Audit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 业务ID
     */
    private Long flowNo;

    /**
     * 审核类型 （1.api，2.xxx）
     */
    private Integer type;

    /**
     * 审核进度（1.待审核，2.通过，3.驳回，4.撤销）
     */
    private Integer status;

}
