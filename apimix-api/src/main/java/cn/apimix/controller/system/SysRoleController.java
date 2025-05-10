package cn.apimix.controller.system;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.user.model.req.menu.AssignMenuRequest;
import cn.apimix.user.model.req.role.SysRoleAddRequest;
import cn.apimix.user.model.req.role.SysRoleEditRequest;
import cn.apimix.user.model.req.role.SysRoleQueryRequest;
import cn.apimix.user.model.entity.Role;
import cn.apimix.user.model.mapstruct.RoleMapping;
import cn.apimix.model.vo.role.RoleVo;
import cn.apimix.user.service.impl.RoleMenuServiceImpl;
import cn.apimix.user.service.impl.RoleServiceImpl;
import cn.apimix.user.service.impl.UserRoleServiceImpl;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

/**
 * @Author: Hor
 * @Date: 2024/5/24 20:10
 * @Version: 1.0
 */
@RestController
@ResponseResult
@RequestMapping("/api/system/role")
public class SysRoleController {

    @Resource
    private RoleServiceImpl roleService;

    @Resource
    private RoleMenuServiceImpl roleMenuService;

    @Resource
    private RoleMapping roleMapping;

    @Resource
    private UserRoleServiceImpl userRoleService;

    /**
     * 新增角色
     */
    @SaCheckLogin
    @SaCheckPermission("sys:role:add")
    @PostMapping()
    public Boolean add(@RequestBody @Valid SysRoleAddRequest addRequest) {
        return roleService.insertRole(addRequest);
    }

    /**
     * 获取角色列表
     */
    @SaCheckLogin
    @SaCheckPermission("sys:role:list")
    @GetMapping()
    public Page<Role> pageList(@Valid SysRoleQueryRequest queryRequest) {
        return roleService.selectRoleByPage(queryRequest);
    }

    /**
     * 修改角色
     */
    @SaCheckLogin
    @SaCheckPermission("sys:role:edit")
    @PutMapping("/{id}")
    public Boolean edit(@PathVariable Integer id,@RequestBody @Valid SysRoleEditRequest editRequest) {
        Assert.isTrue(Objects.equals(id, editRequest.getId()),"参数错误");
        roleMenuService.insertRoleMenus(editRequest.getId(), editRequest.getMenuIds());
        return roleService.updateRole(editRequest);
    }

    /**
     * 删除角色
     */
    @SaCheckLogin
    @SaCheckPermission("sys:role:del")
    @DeleteMapping("/{id}")
    public Boolean del(@PathVariable Integer id) {
        return roleService.deleteRoleById(id);
    }

    /**
     * 获取角色详情
     */
    @SaCheckLogin
    @SaCheckPermission("sys:role:query")
    @GetMapping(value = "/{id}")
    public RoleVo getRoleInfo(@PathVariable Long id) {
        RoleVo roleVo = RoleVo.builder().build();
        Role role = roleService.getById(id);
        List<Integer> integers = roleMenuService.selectRoleMenuIdByRoleId(role.getId());

        BeanUtil.copyProperties(role, roleVo);
        roleVo.setMenuIds(integers);
        return roleVo;
    }

    @SaCheckLogin
    @SaCheckPermission("sys:role:query")
    @GetMapping("/{id}/user")
    public List<String> getUserRole(@PathVariable Integer id) {
        return userRoleService.selectUserIdsByRoleId(id);
    }

    @SaCheckLogin
    @SaCheckPermission("sys:role:add")
    @PostMapping("/{roleId}/user")
    public void postUserRole(@PathVariable Integer roleId,@RequestBody List<Long> userIds) {
        userRoleService.insertBatchUserRole(roleId, userIds);
    }


    /**
     * 获取角色权限
     */
    @SaCheckLogin
    @GetMapping("menuIds")
    public List<Integer> getSystemRoleMenuIds(@RequestParam @NotBlank(message = "roleId不能为空") Integer roleId) {
        return roleMenuService.selectRoleMenuIdByRoleId(roleId);
    }

    /**
     * 角色分配菜单
     */
    @SaCheckLogin
    @SaCheckPermission("sys:role:query")
    @PostMapping("assignMenu")
    public Boolean assignMenu(@RequestBody @Valid AssignMenuRequest assignMenuRequest) {
        return roleMenuService.insertRoleMenus(assignMenuRequest.getRoleId(), assignMenuRequest.getMenuIds());
    }

}
