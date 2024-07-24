package cn.apimix.model.vo.api;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author: Hor
 * @Date: 2024/6/3 22:39
 * @Version: 1.0
 */
@Data
@Builder
public class AuditRecordVo {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 审核人员ID
     */
    private Long userId;

    /**
     * 审核进度（1.待审核，2.通过，3.驳回，4.撤销）
     */
    private Integer status;

    /**
     * 审核时间
     */
    private Date createTime;

}
