package cn.apimix.service;


import cn.apimix.model.entity.RoleMenu;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/24 20:52
 * @Version: 1.0
 */
public interface IRoleMenuService extends IService<RoleMenu> {

    /**
     * 通过角色ID删除角色和菜单关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    Boolean deleteRoleMenuByRoleId(Integer roleId);

    /**
     * 批量删除角色菜单关联信息
     *
     * @param ids 需要删除的数据IDs
     * @return 结果
     */
    Boolean deleteRoleMenuByRoleIds(List<Integer> ids);

    /**
     * 根据角色code查询该角色已分配的菜单权限
     *
     * @param roleCode 角色code
     * @return List<Integer> 菜单ID列表
     */

    List<Integer> selectRoleMenuByRoleCode(String roleCode);

    /**
     * 根据角色ID查询该角色已分配的菜单权限
     *
     * @param roleId 角色id
     * @return List<Integer> 菜单ID列表
     */
    List<Integer> selectRoleMenuIdByRoleId(Integer roleId);


    /**
     * 根据角色ID和菜单ID新增
     */
    Boolean insertRoleMenu(Integer roleId, Integer menuId);

    /**
     * 工具角色ID和菜单ID列表新增
     */
    Boolean insertRoleMenus(Integer roleId, List<Integer> menuIds);

}
