package cn.apimix.service.impl;

import cn.apimix.mapper.RoleMapper;
import cn.apimix.model.dto.system.role.SysRoleEditRequest;
import cn.apimix.model.dto.system.role.SysRoleQueryRequest;
import cn.apimix.model.entity.Role;
import cn.apimix.model.entity.table.RoleTableDef;
import cn.apimix.model.entity.table.UserRoleTableDef;
import cn.apimix.model.enums.RoleTypeEnum;
import cn.apimix.model.mapstruct.RoleMapping;
import cn.apimix.service.IRoleService;
import cn.hutool.core.lang.Assert;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.If;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.StringUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/20 22:37
 * @Version: 1.0
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private RoleMapping roleMapping;

    @Resource
    private RoleMenuServiceImpl roleMenuService;

    /**
     * 新增角色
     *
     * @param role 角色实体
     * @return 是否成功
     */
    @Override
    public Boolean insertRole(Role role) {
        // 1. 判断角色是否存在
        boolean codeExists = queryChain()
                .where(RoleTableDef.ROLE.CODE.eq(role.getCode()))
                .exists();
        Assert.isFalse(codeExists, "角色" + role.getCode() + "已存在");
        // 2. 插入数据库
        return save(role);
    }

    /**
     * 通过角色的ID去删除角色
     *
     * @param roleId 角色实体
     * @return 是否成功
     */
    @Transactional
    @Override
    public Boolean deleteRoleById(Integer roleId) {
        // 获取角色
        Role role = getById(roleId);
        // 判断角色是否能被删除
        Assert.isFalse(role.getType() == RoleTypeEnum.INTERNAL.getType(), "内置角色无法删除");
        // 判断角色是否被分配
        boolean exists = queryChain()
                .from(UserRoleTableDef.USER_ROLE)
                .where(UserRoleTableDef.USER_ROLE.ROLE_ID.eq(roleId))
                .exists();
        Assert.isFalse(exists, "角色[" + role.getName() + "] 已分配，不能删除");

        // 删除与该角色关联的数据，角色与菜单的关联数据
        roleMenuService.deleteRoleMenuByRoleId(roleId);
        // 3. 删除角色
        QueryWrapper queryWrapper = QueryWrapper.create()
                .from(RoleTableDef.ROLE)
                .where(RoleTableDef.ROLE.ID.eq(roleId));
        return remove(queryWrapper);
    }

    /**
     * 通过角色ID列表去删除角色
     *
     * @param roleIds 角色ID列表
     * @return 结果
     */
    @Override
    public Boolean deleteRoleByIds(List<Integer> roleIds) {
        // 判断该角色是否已经分配给用户 已分配的不能删除
        for (Integer id : roleIds) {
            boolean exists = queryChain().from(UserRoleTableDef.USER_ROLE).where(UserRoleTableDef.USER_ROLE.ROLE_ID.eq(id)).exists();
            Assert.isFalse(exists, "角色[" + id + "] 已分配，不能删除");
        }
        // 删除与该角色关联的数据，角色与菜单的关联数据
        roleMenuService.deleteRoleMenuByRoleIds(roleIds);
        // 删除角色
        QueryWrapper queryWrapper = QueryWrapper.create()
                .from(RoleTableDef.ROLE)
                .where(RoleTableDef.ROLE.ID.in(roleIds));
        return remove(queryWrapper);
    }

    /**
     * 修改保持角色信息
     *
     * @param editRequest 角色实体
     * @return 结果
     */
    @Override
    public Boolean updateRole(SysRoleEditRequest editRequest) {
        // 转换为Role
        Role role = roleMapping.sysRoleEditRequestToRole(editRequest);
        return updateById(role);
    }

    /**
     * 分页获取角色列表
     *
     * @param queryRequest 分页请求
     * @return 分页结果集
     */
    @Override
    public Page<Role> selectRoleByPage(SysRoleQueryRequest queryRequest) {
        Page<Role> page = Page.of(queryRequest.getPageNumber(), queryRequest.getPageSize());
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(RoleTableDef.ROLE.NAME.like(queryRequest.getName(), StringUtil::isNotBlank))
                .and(RoleTableDef.ROLE.STATUS.eq(queryRequest.getStatus(), If::notNull));
        return page(page, queryWrapper);
    }

    /**
     * 根据角色code列表获取角色名称
     *
     * @param roleCode 角色Code列表
     * @return 角色name结果
     */
    @Override
    public List<String> selectRoleNameByRoleCode(List<String> roleCode) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(RoleTableDef.ROLE.NAME)
                .where(RoleTableDef.ROLE.CODE.in(roleCode));
        return listAs(queryWrapper, String.class);
    }

    /**
     * 根据角色code列表获取角色列表
     *
     * @param roleCode 角色Code列表
     * @return 角色结果
     */
    @Override
    public List<Role> selectRoleByRoleCode(List<String> roleCode) {
        QueryWrapper roleIdQueryWrapper = QueryWrapper.create()
                .where(RoleTableDef.ROLE.CODE.in(roleCode));
        return list(roleIdQueryWrapper);
    }

    /**
     * 根据角色Code列表获取角色ID列表
     *
     * @param roleCodes 角色Code列表
     * @return 角色ID结果
     */
    @Override
    public List<Integer> selectRoleIdByRoleCode(List<String> roleCodes) {
        QueryWrapper roleIdQueryWrapper = QueryWrapper.create()
                .select(RoleTableDef.ROLE.ID)
                .where(RoleTableDef.ROLE.CODE.in(roleCodes));
        return listAs(roleIdQueryWrapper, Integer.class);
    }

    /**
     * 根据用户ID获取角色code
     *
     * @param userId 用户ID
     * @return 角色code结果集
     */
    @Override
    public List<String> selectRoleCodeByUserId(Long userId) {
        // 构建查询条件
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(RoleTableDef.ROLE.CODE)
                .from(RoleTableDef.ROLE)
                .leftJoin(UserRoleTableDef.USER_ROLE)
                .on(RoleTableDef.ROLE.ID.eq(UserRoleTableDef.USER_ROLE.ROLE_ID))
                .where(UserRoleTableDef.USER_ROLE.USER_ID.eq(userId))
                // 角色状态正常
                .and(RoleTableDef.ROLE.STATUS.eq(1));
        return listAs(queryWrapper, String.class);
    }
}
