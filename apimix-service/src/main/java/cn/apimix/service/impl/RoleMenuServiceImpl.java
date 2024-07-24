package cn.apimix.service.impl;

import cn.apimix.mapper.RoleMenuMapper;
import cn.apimix.model.entity.RoleMenu;
import cn.apimix.model.entity.table.RoleMenuTableDef;
import cn.apimix.model.entity.table.RoleTableDef;
import cn.apimix.service.IRoleMenuService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/24 21:01
 * @Version: 1.0
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {

    /**
     * 通过角色ID删除角色和菜单关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public Boolean deleteRoleMenuByRoleId(Integer roleId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(RoleMenuTableDef.ROLE_MENU.ROLE_ID.eq(roleId));
        return remove(queryWrapper);
    }

    /**
     * 批量删除角色菜单关联信息
     *
     * @param ids 需要删除的数据IDs
     * @return 结果
     */
    @Override
    public Boolean deleteRoleMenuByRoleIds(List<Integer> ids) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(RoleMenuTableDef.ROLE_MENU.ROLE_ID.in(ids));
        return remove(queryWrapper);
    }

    /**
     * 根据角色code查询该角色已分配的菜单权限
     *
     * @param roleCode 角色code
     * @return List<Integer> 菜单ID列表
     */
    @Override
    public List<Integer> selectRoleMenuByRoleCode(String roleCode) {
        // 根据角色code查询到该角色的ID
        Integer roleId = getOneAs(
                QueryWrapper.create()
                        .select(RoleTableDef.ROLE.ID)
                        .from(RoleTableDef.ROLE)
                        .where(RoleTableDef.ROLE.CODE.eq(roleCode))
                , Integer.class
        );
        // 根据获取到的角色id获取分配的菜单id
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(RoleMenuTableDef.ROLE_MENU.MENU_ID)
                .where(RoleMenuTableDef.ROLE_MENU.ROLE_ID.eq(roleId));
        return listAs(queryWrapper, Integer.class);
    }

    /**
     * 根据角色ID查询该角色已分配的菜单权限
     *
     * @param roleId 角色id
     * @return List<Integer> 菜单ID列表
     */
    @Override
    public List<Integer> selectRoleMenuIdByRoleId(Integer roleId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(RoleMenuTableDef.ROLE_MENU.MENU_ID)
                .where(RoleMenuTableDef.ROLE_MENU.ROLE_ID.eq(roleId));
        return listAs(queryWrapper, Integer.class);
    }

    /**
     * 根据角色ID和菜单ID新增
     *
     * @param roleId 角色ID
     * @param menuId 菜单ID
     */
    @Override
    public Boolean insertRoleMenu(Integer roleId, Integer menuId) {
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setRoleId(roleId);
        roleMenu.setMenuId(menuId);
        return save(roleMenu);
    }

    /**
     * 工具角色ID和菜单ID列表新增
     *
     * @param roleId  角色ID
     * @param menuIds 菜单IDs
     */
    @Override
    public Boolean insertRoleMenus(Integer roleId, List<Integer> menuIds) {
        // 待添加的菜单ID列表
        List<RoleMenu> roleMenus = new ArrayList<>();
        for (Integer menuId : menuIds) {
            RoleMenu roleMenu = RoleMenu.builder()
                    .roleId(roleId)
                    .menuId(menuId)
                    .build();
            roleMenus.add(roleMenu);
        }
        // 将该用户分配的菜单删除
        remove(QueryWrapper.create().where(RoleMenuTableDef.ROLE_MENU.ROLE_ID.eq(roleId)));
        return saveBatch(roleMenus);
    }
}
