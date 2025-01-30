package cn.apimix.model.dto.system.role;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 分配菜单请求
 *
 * @Author: Hor
 * @Date: 2024/5/20 17:09
 * @Version: 1.0
 */
@Data
public class SysRoleAsMenuRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 角色
     */
    @NotNull(message = "角色ID不能为空")
    private Integer roleId;
    /**
     * 菜单ID列表
     */
    private List<Integer> menuIds;
}
