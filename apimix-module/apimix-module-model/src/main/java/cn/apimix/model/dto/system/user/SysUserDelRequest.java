package cn.apimix.model.dto.system.user;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/24 22:19
 * @Version: 1.0
 */
@Data
public class SysUserDelRequest {
    /**
     * 用户ID
     */
    @NotNull(message = "id 不能为空")
    List<String> ids;
}
