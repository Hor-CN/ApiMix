package cn.apimix.service;

import cn.apimix.model.entity.Menu;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/22 21:30
 * @Version: 1.0
 */
public interface IMenuService extends IService<Menu> {


    /**
     * 获取全部菜单列表
     *
     * @return 菜单列表
     */
    List<Menu> selectMenuByAll();

    /**
     * 获取全部菜单列表
     *
     * @param title  菜单名称
     * @param status 菜单状态
     * @return 菜单列表
     */
    List<Menu> selectMenuByTitleAndStatus(String title, Integer status);


    /**
     * 根据用户ID查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<Menu> selectMenuList(Long userId);


    /**
     * 获取全部的菜单树
     *
     * @return 菜单列表
     */
    List<Menu> selectMenuTreeByAll();


    /**
     * 根据用户角色列表查询菜单
     *
     * @param roleList 角色列表
     * @return List<Menu> 菜单列表
     */
    List<Menu> selectMenuTreeByRoleList(List<String> roleList);


    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    Menu selectMenuById(Long menuId);


    /**
     * 根据角色code获取权限列表
     *
     * @param roleCode 角色
     * @return List<String> 权限列表
     */
    List<String> selectPermsByRoleCode(String roleCode);


    /**
     * 根据角色code列表获取权限列表
     *
     * @param roleCodeList 角色
     * @return List<String> 权限列表
     */
    List<String> selectPermsByRoleCodes(List<String> roleCodeList);

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    Boolean deleteMenuById(Long menuId);

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    Boolean updateMenu(Menu menu);

}
