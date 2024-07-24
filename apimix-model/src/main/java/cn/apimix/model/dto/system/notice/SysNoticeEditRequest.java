package cn.apimix.model.dto.system.notice;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Hor
 * @Date: 2024/5/25 22:23
 * @Version: 1.0
 */
@Data
public class SysNoticeEditRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告类型（1通知 2公告）
     */
    private Integer type;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 公告状态（0正常 1关闭）
     */
    private Boolean status;

}
