package cn.apimix.model.dto.system.user;

import cn.apimix.core.core.model.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户搜索请求
 *
 * @Author: Hor
 * @Date: 2024/5/20 19:35
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户状态
     */
    private Boolean status;

    /**
     * 用户名称
     */
    private String username;
}
