package cn.apimix.service.impl;

import cn.apimix.mapper.UserRoleMapper;
import cn.apimix.model.entity.UserRole;
import cn.apimix.model.entity.table.UserRoleTableDef;
import cn.apimix.service.IUserRoleService;
import cn.hutool.core.collection.CollUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Hor
 * @Date: 2024/5/21 20:14
 * @Version: 1.0
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {
    /**
     * 根据用户ID删除用户与角色关联
     *
     * @param userId 用户ID
     * @return boolean
     */
    @Override
    public Boolean deleteUserRole(Long userId) {
        QueryWrapper removeUserRoleQueryWrapper = QueryWrapper.create()
                .where(UserRoleTableDef.USER_ROLE.USER_ID.eq(userId));
        return remove(removeUserRoleQueryWrapper);
    }

    /**
     * 批量新增用户和角色关联
     *
     * @param userId  用户ID
     * @param roleIds 角色ID列表
     * @return Boolean
     */
    @Override
    public Boolean insertBatchUserRole(Long userId, List<Integer> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return false;
        }
        List<UserRole> userRoles = roleIds.stream()
                .map(roleId -> UserRole.builder()
                        // 设置用户ID和角色ID
                        .userId(userId)
                        .roleId(roleId)
                        .build())
                .collect(Collectors.toList());
        return saveBatch(userRoles);
    }
}
