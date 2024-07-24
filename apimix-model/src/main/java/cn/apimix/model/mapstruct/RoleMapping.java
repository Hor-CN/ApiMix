package cn.apimix.model.mapstruct;

import cn.apimix.model.dto.system.role.SysRoleAddRequest;
import cn.apimix.model.dto.system.role.SysRoleEditRequest;
import cn.apimix.model.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @Author: Hor
 * @Date: 2024/5/22 21:21
 * @Version: 1.0
 */
@Mapper(componentModel = "spring")
public interface RoleMapping {

    @Mapping(target = "disabled", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    Role sysRoleEditRequestToRole(SysRoleEditRequest sysRoleEditRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "disabled", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    Role sysRoleAddRequestToRole(SysRoleAddRequest sysRoleAddRequest);
}
