package cn.apimix.controller.system.role;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.core.core.model.IdsRequest;
import cn.apimix.model.dto.system.menu.AssignMenuRequest;
import cn.apimix.model.dto.system.role.SysRoleAddRequest;
import cn.apimix.model.dto.system.role.SysRoleEditRequest;
import cn.apimix.model.dto.system.role.SysRoleQueryRequest;
import cn.apimix.model.entity.Role;
import cn.apimix.model.mapstruct.RoleMapping;
import cn.apimix.model.vo.role.RoleVo;
import cn.apimix.service.impl.RoleMenuServiceImpl;
import cn.apimix.service.impl.RoleServiceImpl;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.bean.BeanUtil;
import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

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

    /**
     * 新增角色
     */
    @SaCheckLogin
    @SaCheckPermission("sys:role:add")
    @PostMapping("save")
    public Boolean add(@RequestBody @Valid SysRoleAddRequest addRequest) {
        Role role = roleMapping.sysRoleAddRequestToRole(addRequest);
        return roleService.insertRole(role);
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
    @PostMapping("edit")
    public Boolean edit(@RequestBody @Valid SysRoleEditRequest editRequest) {
        return roleService.updateRole(editRequest);
    }

    /**
     * 删除角色
     */
    @SaCheckLogin
    @SaCheckPermission("sys:role:del")
    @PostMapping("delete")
    public Boolean del(@RequestBody @Valid IdsRequest ids) {
        return roleService.deleteRoleByIds(ids.getIds());
    }

    /**
     * 获取角色详情
     */
    @SaCheckLogin
    @SaCheckPermission("sys:role:query")
    @GetMapping("detail")
    public RoleVo getInfo(@RequestParam @NotBlank(message = "ID不能为空") Integer id) {
        RoleVo roleVo = RoleVo.builder().build();
        Role role = roleService.getById(id);
        BeanUtil.copyProperties(role, roleVo);
        return roleVo;
    }


    /**
     * 获取角色权限
     */
    @SaCheckLogin
    @GetMapping("menuIds")
    public List<Integer> getSystemRoleMenuIds(
            @RequestParam @NotBlank(message = "roleId不能为空") Integer roleId
    ) {
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
