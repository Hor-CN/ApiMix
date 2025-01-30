package cn.apimix.controller;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.model.dto.system.option.OptionQuery;
import cn.apimix.model.entity.Menu;
import cn.apimix.model.enums.OptionCategoryEnum;
import cn.apimix.model.mapstruct.MenuMapping;
import cn.apimix.model.vo.LabelValueResp;
import cn.apimix.model.vo.LabelValueState;
import cn.apimix.model.vo.menu.MenuTreeSelectVo;
import cn.apimix.service.impl.MenuServiceImpl;
import cn.apimix.service.impl.RoleServiceImpl;
import cn.apimix.service.impl.SysOptionServiceImpl;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Hor
 * @Date: 2025/1/2 09:52
 * @Version: 1.0
 */

@RestController
@ResponseResult
@RequestMapping("/api/common")
public class CommonController {

    @Resource
    private MenuServiceImpl menuService;

    @Resource
    private MenuMapping menuMapping;

    @Resource
    private RoleServiceImpl roleService;

    @Resource
    private SysOptionServiceImpl optionService;


    /**
     * 获取角色分配的权限菜单树
     *
     * @return 菜单权限树
     */
    @SaCheckLogin
    @SaCheckPermission("sys:menu:list")
    @GetMapping("tree/menu")
    public List<MenuTreeSelectVo> roleMenuTreeSelect() {
        List<Menu> menus = menuService.selectMenuByAll();
        return menuMapping.menuToTreeSelectVos(menus);
    }

    /**
     * 查询角色列表
     *
     * @return 角色列表
     */
    @SaCheckLogin
    @SaCheckPermission("sys:role:list")
    @GetMapping("dict/role")
    public List<LabelValueState> roleDict() {
        return roleService.list().stream().map(item -> LabelValueState.builder()
                .label(item.getName())
                .value(Long.valueOf(item.getId()))
                .build()).collect(Collectors.toList());
    }



    @GetMapping("/dict/option/site")
//    @Cached(key = "'SITE'", name = CacheConstants.OPTION_KEY_PREFIX)
    public List<LabelValueResp<String>> listSiteOptionDict() {
        OptionQuery optionQuery = new OptionQuery();
        optionQuery.setCategory(OptionCategoryEnum.SITE.name());
        return optionService.list(optionQuery)
                .stream()
                .map(option -> new LabelValueResp<>(option.getCode(), StrUtil.nullToDefault(option.getValue(), option
                        .getDefaultValue())))
                .collect(Collectors.toList());
    }


}
