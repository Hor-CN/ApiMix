package cn.apimix.service;

import cn.apimix.model.dto.system.role.SysRoleEditRequest;
import cn.apimix.model.dto.system.role.SysRoleQueryRequest;
import cn.apimix.model.entity.Role;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * 服务层。
 *
 * @Author: Hor
 * @Date: 2024/5/20 22:33
 * @Version: 1.0
 */
public interface IRoleService extends IService<Role> {

    /**
     * 新增角色
     *
     * @param role 角色实体
     * @return 是否成功
     */
    Boolean insertRole(Role role);


    /**
     * 通过角色的ID去删除角色
     *
     * @param roleId 角色ID
     * @return 是否成功
     */
    Boolean deleteRoleById(Integer roleId);


    /**
     * 通过角色ID列表去删除角色
     *
     * @param roleIds 角色ID列表
     * @return 结果
     */
    Boolean deleteRoleByIds(List<Integer>  roleIds);

    /**
     * 修改保持角色信息
     *
     * @param editRequest 角色实体
     * @return 结果
     */
    Boolean updateRole(SysRoleEditRequest editRequest);


    /**
     * 分页获取角色列表
     *
     * @param queryRequest 分页请求
     * @return 分页结果集
     */
    Page<Role> selectRoleByPage(SysRoleQueryRequest queryRequest);


    /**
     * 根据角色code列表获取角色名称
     *
     * @param roleCode 角色Code列表
     * @return 角色name结果
     */
    List<String> selectRoleNameByRoleCode(List<String> roleCode);


    /**
     * 根据角色code列表获取角色列表
     *
     * @param roleCode 角色Code列表
     * @return 角色结果
     */
    List<Role> selectRoleByRoleCode(List<String> roleCode);


    /**
     * 根据角色Code列表获取角色ID列表
     *
     * @param roleCodes 角色Code列表
     * @return 角色ID结果
     */
    List<Integer> selectRoleIdByRoleCode(List<String> roleCodes);


    /**
     * 根据用户ID获取角色code
     *
     * @param userId 用户ID
     * @return 角色code结果集
     */
    List<String> selectRoleCodeByUserId(Long userId);

}
