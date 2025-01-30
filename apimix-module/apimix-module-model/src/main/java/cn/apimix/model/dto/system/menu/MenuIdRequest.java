package cn.apimix.model.dto.system.menu;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @Author: Hor
 * @Date: 2024/5/24 19:08
 * @Version: 1.0
 */
@Data
public class MenuIdRequest {
    @NotBlank(message = "ID不能为空")
    @Pattern(regexp = "\\d+", message = "ID 必须是数字")
    String id;
}