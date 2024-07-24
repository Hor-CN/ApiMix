package cn.apimix.service.impl;

import cn.apimix.mapper.MenuMapper;
import cn.apimix.model.entity.Menu;
import cn.apimix.model.entity.table.MenuTableDef;
import cn.apimix.model.entity.table.RoleMenuTableDef;
import cn.apimix.model.entity.table.RoleTableDef;
import cn.apimix.service.IMenuService;
import cn.hutool.core.lang.Assert;
import com.mybatisflex.core.query.If;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.StringUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/22 22:04
 * @Version: 1.0
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Resource
    private RoleServiceImpl roleService;

    /**
     * 获取全部菜单列表
     *
     * @return 菜单列表
     */
    @Override
    public List<Menu> selectMenuByAll() {
        return selectMenuByTitleAndStatus(null, null);
    }

    /**
     * 获取全部菜单列表
     *
     * @param title  菜单名称
     * @param status 菜单状态
     * @return 菜单列表
     */
    @Override
    public List<Menu> selectMenuByTitleAndStatus(String title, Integer status) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(MenuTableDef.MENU.PARENT_ID.eq(0))
                .and(MenuTableDef.MENU.TITLE.like(title, StringUtil::isNotBlank))
                .and(MenuTableDef.MENU.STATUS.eq(status, If::notNull))
                // 排序
                .orderBy(MenuTableDef.MENU.PARENT_ID.asc(), MenuTableDef.MENU.SORT.asc());
        return getMapper().selectListWithRelationsByQuery(queryWrapper);
    }

    /**
     * 根据用户ID查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<Menu> selectMenuList(Long userId) {
        return selectMenuTreeByRoleList(roleService.selectRoleCodeByUserId(userId));
    }

    /**
     * 获取全部的菜单树
     *
     * @return 菜单列表
     */
    @Override
    public List<Menu> selectMenuTreeByAll() {
        // 1. 获取顶级菜单
        QueryWrapper um = QueryWrapper.create()
                .select(MenuTableDef.MENU.ALL_COLUMNS)
                .from(MenuTableDef.MENU)
                // 父菜单只能是顶级菜单
                .where(MenuTableDef.MENU.PARENT_ID.eq(0))
                // 菜单的状态为正常
                .and(MenuTableDef.MENU.STATUS.eq(true))
                // 菜单的类型只能是(目录1|菜单2) 去除 按钮(权限)
                .and(MenuTableDef.MENU.TYPE.in(1,2))
                // 菜单的显示顺序
                .orderBy(MenuTableDef.MENU.ID.asc(),MenuTableDef.MENU.SORT.asc());

        // 2. 获取子菜单
        @SuppressWarnings("unchecked")
        List<Menu> menus = mapper.selectListByQuery(um,
                // 子菜单
                fieldQueryBuilder -> fieldQueryBuilder
                        .field(Menu::getChildren)
                        .queryWrapper(
                                menu -> QueryWrapper
                                        .create()
                                        .select()
                                        .from(MenuTableDef.MENU)
                                        .where(MenuTableDef.MENU.PARENT_ID.eq(menu.getId()))
                                        .and(MenuTableDef.MENU.PARENT_ID.ne(0))
                                        .and(MenuTableDef.MENU.STATUS.eq(true))
                                        // 4. 菜单的类型只能是(目录1|菜单2) 去除 按钮(权限)
                                        .and(MenuTableDef.MENU.TYPE.in(1,2))
                                        .orderBy(MenuTableDef.MENU.PARENT_ID.asc(),MenuTableDef.MENU.SORT.asc())
                        )
        );
        return menus;
    }

    /**
     * 根据用户角色列表查询菜单
     *
     * @param roleList 角色列表
     * @return List<Menu> 菜单列表
     */
    @Override
    public List<Menu> selectMenuTreeByRoleList(List<String> roleList) {
        Assert.notEmpty(roleList, "用户状态异常,请联系管理员");
        // 获取顶级菜单
        QueryWrapper um = QueryWrapper.create()
                .select(MenuTableDef.MENU.ALL_COLUMNS)
                .from(MenuTableDef.MENU)
                // 根据角色获取角色表对应的角色ID
                .leftJoin(RoleTableDef.ROLE)
                .on(RoleTableDef.ROLE.CODE.in(roleList))
                // 根据角色的ID筛选角色菜单表
                .leftJoin(RoleMenuTableDef.ROLE_MENU)
                .on(RoleTableDef.ROLE.ID.eq(RoleMenuTableDef.ROLE_MENU.ROLE_ID))
                // 1. 父菜单只能是顶级菜单
                .where(MenuTableDef.MENU.PARENT_ID.eq(0))
                .and(MenuTableDef.MENU.ID.eq(RoleMenuTableDef.ROLE_MENU.MENU_ID))
                // 2. 菜单是否隐藏
                .and(MenuTableDef.MENU.HIDDEN.eq(false))
                // 3. 菜单的状态为正常
                .and(MenuTableDef.MENU.STATUS.eq(true))
                // 4. 菜单的类型只能是(目录1|菜单2) 去除 按钮(权限)
                .and(MenuTableDef.MENU.TYPE.in(1, 2))
                // 5. 菜单的显示顺序
                .orderBy(MenuTableDef.MENU.ID.asc(), MenuTableDef.MENU.SORT.asc());
        // 获取子菜单
        @SuppressWarnings("unchecked")
        List<Menu> menus = mapper.selectListByQuery(um,
                // 子菜单
                fieldQueryBuilder -> fieldQueryBuilder
                        .field(Menu::getChildren)
                        .queryWrapper(
                                menu -> QueryWrapper
                                        .create()
                                        .select()
                                        .from(MenuTableDef.MENU)
                                        .join(RoleMenuTableDef.ROLE_MENU)
                                        .on(RoleMenuTableDef.ROLE_MENU.MENU_ID.eq(MenuTableDef.MENU.ID))
                                        .join(RoleTableDef.ROLE)
                                        .on(RoleTableDef.ROLE.ID.eq(RoleMenuTableDef.ROLE_MENU.ROLE_ID))
                                        .where(RoleTableDef.ROLE.CODE.in(roleList))
                                        .and(MenuTableDef.MENU.PARENT_ID.eq(menu.getId()))
                                        // 1. 不能是顶级菜单
                                        .and(MenuTableDef.MENU.PARENT_ID.ne(0))
                                        // 2. 菜单是否隐藏
//                                        .and(MenuTableDef.MENU.HIDDEN.eq(false))
                                        // 3. 菜单的状态为正常
                                        .and(MenuTableDef.MENU.STATUS.eq(true))
                                        // 4. 菜单的类型只能是(目录1|菜单2) 去除 按钮(权限)
                                        .and(MenuTableDef.MENU.TYPE.in(1, 2))
                                        .orderBy(MenuTableDef.MENU.PARENT_ID.asc(), MenuTableDef.MENU.SORT.asc())
                        )
        );
        return menus;
    }

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    @Override
    public Menu selectMenuById(Long menuId) {
        return getById(menuId);
    }

    /**
     * 根据角色code获取权限
     *
     * @param roleCode 角色
     * @return List<String> 权限列表
     */
    @Override
    public List<String> selectPermsByRoleCode(String roleCode) {
        return selectPermsByRoleCodes(Collections.singletonList(roleCode));
    }

    /**
     * 根据角色code列表获取权限列表
     *
     * @param roleCodeList 角色
     * @return List<String> 权限列表
     */
    @Override
    public List<String> selectPermsByRoleCodes(List<String> roleCodeList) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(QueryMethods.distinct(MenuTableDef.MENU.PERMISSION))
                .leftJoin(RoleMenuTableDef.ROLE_MENU)
                .on(MenuTableDef.MENU.ID.eq(RoleMenuTableDef.ROLE_MENU.MENU_ID))
                .leftJoin(RoleTableDef.ROLE)
                .on(RoleTableDef.ROLE.ID.eq(RoleMenuTableDef.ROLE_MENU.ROLE_ID))
                .where(MenuTableDef.MENU.PERMISSION.isNotNull())
                .and(RoleTableDef.ROLE.CODE.in(roleCodeList))
                .and(MenuTableDef.MENU.HIDDEN.eq(0));
        return listAs(queryWrapper, String.class);
    }

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public Boolean deleteMenuById(Long menuId) {
        // 判断该菜单是否存在子菜单，若存在未被删除的子菜单，不能删除
        boolean existsId = queryChain().where(MenuTableDef.MENU.PARENT_ID.eq(menuId)).exists();
        Assert.isFalse(existsId, "存在子菜单,不允许删除");
        // 判断该菜单是否已经被分配，若已经分配角色，不能删除
        boolean existsRole = queryChain().from(RoleMenuTableDef.ROLE_MENU).where(RoleMenuTableDef.ROLE_MENU.MENU_ID.eq(menuId)).exists();
        Assert.isFalse(existsRole, "菜单已分配,不允许删除");
        // 删除该菜单
        return removeById(menuId);
    }

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public Boolean updateMenu(Menu menu) {
        // 判断是否已经存在同名菜单除去自己，只查当前菜单的类型
        boolean existsTitle = queryChain()
                .where(MenuTableDef.MENU.TITLE.eq(menu.getTitle()))
                .and(MenuTableDef.MENU.ID.ne(menu.getId()))
                .and(MenuTableDef.MENU.TYPE.eq(menu.getType()))
                .exists();
        Assert.isFalse(existsTitle, "修改菜单'" + menu.getTitle() + "'失败，菜单名称已存在");
        // 上级菜单不能选择自己
        Assert.isFalse(menu.getId().equals(menu.getParentId()), "修改菜单'" + menu.getTitle() + "'失败，上级菜单不能选择自己");
        // 更新菜单
        return updateById(menu);
    }
}
