package cn.apimix.model.mapstruct;

import cn.apimix.model.dto.system.user.SysUserEditRequest;
import cn.apimix.model.entity.User;
import cn.apimix.model.vo.user.UserVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * mapstruct User
 *
 * @Author: Hor
 * @Date: 2024/5/21 21:40
 * @Version: 1.0
 */
@Mapper(componentModel = "spring")
public interface UserMapping {

    /**
     * 源类型 目标类型 成员变量相同类型 相同变量名
     *
     * @param userEditRequest the userEditRequest
     * @return the User
     */
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "salt", ignore = true)
    @Mapping(target = "isDelete", ignore = true)
    @Mapping(target = "disabled", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User sysUserEditRequestToUser(SysUserEditRequest userEditRequest);


    @Mapping(target = "username", source = "userName")
    @Mapping(target = "roleNames", ignore = true)
    @Mapping(target = "permission", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "nickname", source = "nickName")
    UserVo userToUserVo(User user);


    List<UserVo> usersToUserVos(List<User> users);
}
