package cn.apimix.model.dto.system.notice;

import cn.apimix.core.core.model.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author: Hor
 * @Date: 2024/5/25 19:53
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysNoticeQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告类型（1通知 2公告）
     */
    private Integer type;

    /**
     * 公告状态（0正常 1关闭）
     */
    private Boolean status;


}
