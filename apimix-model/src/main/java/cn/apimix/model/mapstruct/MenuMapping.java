package cn.apimix.model.mapstruct;

import cn.apimix.model.dto.system.menu.SysMenuAddRequest;
import cn.apimix.model.dto.system.menu.SysMenuEditRequest;
import cn.apimix.model.entity.Menu;
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


    MenuTreeSelectVo menuTreeSelectVo(Menu menu);

    List<MenuTreeSelectVo> menuToTreeSelectVos(List<Menu> menus);


    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "query", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "affix", ignore = true)
    @Mapping(target = "activeMenu", ignore = true)
    Menu menuAddRequestToMenu(SysMenuAddRequest menu);



    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "query", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "affix", ignore = true)
    @Mapping(target = "activeMenu", ignore = true)
    Menu editMenuRequestToMenu(SysMenuEditRequest menu);


}
