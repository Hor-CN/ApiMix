package cn.apimix.model.dto.system.notice;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Author: Hor
 * @Date: 2024/5/25 22:12
 * @Version: 1.0
 */
@Data
public class SysNoticeAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 公告标题
     */
    @NotBlank(message = "公告标题不能为空")
    private String title;

    /**
     * 公告类型（1通知 2公告）
     */
    private Integer type;

    /**
     * 公告内容
     */
    @NotBlank(message = "公告内容不能为空")
    private String content;

    /**
     * 公告状态（0正常 1关闭）
     */
    private Boolean status;

}
