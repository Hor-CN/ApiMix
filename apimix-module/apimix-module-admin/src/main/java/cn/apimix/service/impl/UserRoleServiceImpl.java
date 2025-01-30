package cn.apimix.service.impl;

import cn.apimix.mapper.UserRoleMapper;
import cn.apimix.model.entity.UserRole;
import cn.apimix.model.entity.table.UserRoleTableDef;
import cn.apimix.service.UserRoleService;
import cn.hutool.core.collection.CollUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Hor
 * @Date: 2024/5/21 20:14
 * @Version: 1.0
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
    /**
     * 根据用户ID删除用户与角色关联
     *
     * @param userId 用户ID
     */
    @Override
    public void deleteUserRole(Long userId) {
        QueryWrapper removeUserRoleQueryWrapper = QueryWrapper.create()
                .where(UserRoleTableDef.USER_ROLE.USER_ID.eq(userId));
        remove(removeUserRoleQueryWrapper);
    }

    /**
     * 批量新增用户和角色关联
     *
     * @param userId  用户ID
     * @param roleIds 角色ID列表
     */
    @Override
    public void insertBatchUserRole(Long userId, List<Integer> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return;
        }
        List<UserRole> userRoles = roleIds.stream()
                .map(roleId -> UserRole.builder()
                        // 设置用户ID和角色ID
                        .userId(userId)
                        .roleId(roleId)
                        .build())
                .collect(Collectors.toList());
        saveOrUpdateBatch(userRoles);
    }

    /**
     * 批量新增用户和角色关联
     *
     * @param roleId  角色ID
     * @param userIds 用户ID列表
     */
    @Transactional
    @Override
    public void insertBatchUserRole(Integer roleId, List<Long> userIds) {
        if (CollUtil.isEmpty(userIds)) {
            return;
        }

        // 该用户拥有的角色
        List<Long> userRoles = listAs(query().select(UserRoleTableDef.USER_ROLE.USER_ID).where(UserRoleTableDef.USER_ROLE.ROLE_ID.eq(roleId)), Long.class);

        // 获取要添加的
        List<Long> adds = userIds.stream().filter(id -> !userRoles.contains(id)).collect(Collectors.toList());
        // 获取要删除的
        List<Long> removes = userRoles.stream().filter(id -> !userIds.contains(id)).collect(Collectors.toList());

        if (!adds.isEmpty()) {
            // 新增
            saveBatch(adds.stream().map(id -> UserRole.builder()
                    .userId(id)
                    .roleId(roleId)
                    .build()).collect(Collectors.toList()));
        }

        if (!removes.isEmpty()) {
            // 删除
            remove(query().where(UserRoleTableDef.USER_ROLE.USER_ID.in(removes).and(UserRoleTableDef.USER_ROLE.ROLE_ID.eq(roleId))));
        }

    }

    /**
     * 根据角色ID获取用户id列表
     *
     * @param roleId 角色ID
     * @return 用户ID列表
     */
    @Override
    public List<String> selectUserIdsByRoleId(Integer roleId) {
        return listAs(query().select(UserRoleTableDef.USER_ROLE.USER_ID).where(UserRoleTableDef.USER_ROLE.ROLE_ID.eq(roleId)), String.class);
    }

}
