package cn.apimix.user.service;

import cn.apimix.user.model.entity.UserRole;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/21 20:09
 * @Version: 1.0
 */
public interface UserRoleService extends IService<UserRole> {

    /**
     * 根据用户ID删除用户与角色关联
     *
     * @param userId 用户ID
     */
    void deleteUserRole(Long userId);

    /**
     * 批量新增用户和角色关联
     *
     * @param userId  用户ID
     * @param roleIds 角色ID列表
     */
    void insertBatchUserRole(Long userId, List<Integer> roleIds);

    /**
     * 批量新增用户和角色关联
     *
     * @param roleId  角色ID
     * @param userIds 用户ID列表
     */
    void insertBatchUserRole(Integer roleId, List<Long> userIds);

    /**
     * 根据角色ID获取用户id列表
     * @param roleId 角色ID
     * @return 用户ID列表
     */
    List<String> selectUserIdsByRoleId(Integer roleId);


}
