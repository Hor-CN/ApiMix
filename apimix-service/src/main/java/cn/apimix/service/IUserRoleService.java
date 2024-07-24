package cn.apimix.service;

import cn.apimix.model.entity.UserRole;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/21 20:09
 * @Version: 1.0
 */
public interface IUserRoleService extends IService<UserRole> {

    /**
     * 根据用户ID删除用户与角色关联
     *
     * @param userId 用户ID
     * @return boolean
     */
    Boolean deleteUserRole(Long userId);

    /**
     * 批量新增用户和角色关联
     *
     * @param userId  用户ID
     * @param roleIds 角色ID列表
     * @return Boolean
     */
    Boolean insertBatchUserRole(Long userId, List<Integer> roleIds);

}
