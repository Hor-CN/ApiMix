package cn.apimix.model.dto.system.menu;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/24 21:12
 * @Version: 1.0
 */
@Data
public class AssignMenuRequest {

    @NotNull(message = "roleId不能为空")
    private Integer roleId;
    private List<Integer> menuIds;

}