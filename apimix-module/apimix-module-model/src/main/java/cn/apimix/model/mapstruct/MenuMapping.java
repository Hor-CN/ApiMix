package cn.apimix.model.mapstruct;

import cn.apimix.model.dto.system.menu.SysMenuAddRequest;
import cn.apimix.model.dto.system.menu.SysMenuEditRequest;
import cn.apimix.model.entity.Menu;
import cn.apimix.model.vo.RouteResp;
import cn.apimix.model.vo.menu.MenuTreeSelectVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/24 17:34
 * @Version: 1.0
 */
@Mapper(componentModel = "spring")
public interface MenuMapping {

    @Mapping(target = "key",source = "id")
    MenuTreeSelectVo menuTreeSelectVo(Menu menu);

    List<MenuTreeSelectVo> menuToTreeSelectVos(List<Menu> menus);


    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "children", ignore = true)
    Menu menuAddRequestToMenu(SysMenuAddRequest menu);



    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "children", ignore = true)
    Menu editMenuRequestToMenu(SysMenuEditRequest menu);


    /**
     * 将菜单转为路由
     * @param menu 菜单
     * @return RouteResp 路由
     */
    RouteResp menuToRouteResp(Menu menu);


    /**
     * 菜单列表转路由列表
     * @param menus 菜单列表
     * @return 路由列表
     */
    List<RouteResp> menusToRouteResp(List<Menu> menus);


}
