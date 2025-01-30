package cn.apimix.controller.system;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.core.model.IdRequest;
import cn.apimix.model.dto.system.menu.SysMenuAddRequest;
import cn.apimix.model.dto.system.menu.SysMenuEditRequest;
import cn.apimix.model.entity.Menu;
import cn.apimix.model.mapstruct.MenuMapping;
import cn.apimix.service.impl.MenuServiceImpl;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/23 13:54
 * @Version: 1.0
 */
@RestController
@ResponseResult
@RequestMapping("/api/system/menu")
public class SysMenuController {

    @Resource
    private MenuServiceImpl menuService;

    @Resource
    private MenuMapping menuMapping;


    /**
     * 获取全部菜单列表
     *
     * @return List<Menu> 菜单列表
     */
    @SaCheckLogin
    @SaCheckPermission("sys:menu:list")
    @GetMapping("/tree")
    public List<Menu> getMenuByAll(@RequestParam(required = false) String name, @RequestParam(required = false) Integer status) {
        return menuService.selectMenuByTitleAndStatus(name, status);
    }



    /**
     * 根据菜单编号获取详细信息
     */
    @SaCheckLogin
    @SaCheckPermission("system:menu:query")
    @GetMapping(value = "/{menuId}")
    public Menu getInfo(@PathVariable Long menuId) {
        return menuService.selectMenuById(menuId);
    }

    /**
     * 新增菜单
     */
    @SaCheckLogin
    @SaCheckPermission("sys:menu:add")
    @PostMapping()
    public Boolean add(@RequestBody @Valid SysMenuAddRequest addRequest) {
        Menu menu = menuMapping.menuAddRequestToMenu(addRequest);
        // 1. 如果保存的是目录设置layout
        if (menu.getType() == 1) {
            menu.setComponent("Layout");
        }
        return menuService.save(menu);
    }

    /**
     * 删除菜单
     */
    @SaCheckLogin
    @SaCheckPermission("sys:menu:remove")
    @PostMapping("remove")
    public Boolean remove(@RequestBody IdRequest idRequest) {
        return menuService.removeById(idRequest.getId());
    }

    /**
     * 修改菜单
     */
    @SaCheckLogin
    @SaCheckPermission("sys:menu:edit")
    @PutMapping()
    public Boolean edit(@RequestBody @Valid SysMenuEditRequest editRequest) {
        Menu menu = menuMapping.editMenuRequestToMenu(editRequest);
        return menuService.updateMenu(menu);
    }

}
