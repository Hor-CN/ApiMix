package cn.apimix.model.dto.api;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Hor
 * @Date: 2024/6/3 18:22
 * @Version: 1.0
 */
@Data
public class AuditAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * flowID
     */
    private Long flowNo;

    /**
     * 审核进度（1.待审核，2.通过，3.驳回，4.撤销）
     */
    private Integer status;

    /**
     * 审核备注
     */
    private String remark;

}
