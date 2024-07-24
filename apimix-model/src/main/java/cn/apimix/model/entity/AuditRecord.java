package cn.apimix.model.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 审核明细表
 *
 * @Author: Hor
 * @Date: 2024/5/27 21:54
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "audit_record")
public class AuditRecord {

    /**
     * 主键ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 审核表ID
     */
    private Long auditId;

    /**
     * 审核人ID
     */
    private Long userId;

    /**
     * 审核进度（1.待审核，2.通过，3.驳回，4.撤销）
     */
    private Integer status;

    /**
     * 审核备注
     */
    private String remark;

    /**
     * 审核时间
     */
    private Date createTime;

}
