package cn.apimix.model.dto.system.role;

import cn.apimix.core.core.model.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 角色查询请求
 *
 * @Author: Hor
 * @Date: 2024/5/20 18:27
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色状态
     */
    private Integer status;

}
