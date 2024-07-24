package cn.apimix.service;

import cn.apimix.model.dto.system.user.SysUserAddRequest;
import cn.apimix.model.dto.system.user.SysUserEditRequest;
import cn.apimix.model.dto.system.user.SysUserQueryRequest;
import cn.apimix.model.dto.user.UserEditRequest;
import cn.apimix.model.dto.user.UserLoginRequest;
import cn.apimix.model.entity.User;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * 用户服务层。
 *
 * @Author: Hor
 * @Date: 2024/5/21 14:50
 * @Version: 1.0
 */
public interface IUserService extends IService<User> {


    /**
     * 用户登录
     * @param loginRequest 登录请求信息
     * @return 用户ID
     */
    Long login(UserLoginRequest loginRequest);

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    Page<User> selectUserByPage(SysUserQueryRequest user);


    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    User selectUserById(Long userId);

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    User selectUserByUserName(String userName);


    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    Boolean insertUser(SysUserAddRequest user);


    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    Boolean updateUser(SysUserEditRequest user);


    /**
     * 修改用户信息
     */
    Boolean updateProfile(UserEditRequest editRequest,Long userId);

    /**
     * 重置用户密码
     *
     * @param user 用户
     * @return 结果
     */
    Boolean resetPassword(User user);

    /**
     * 重置用户密码
     *
     * @param userId      用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 结果
     */
    Boolean resetUserPassword(Long userId, String oldPassword, String newPassword);

    /**
     * 重置用户密码
     *
     * @param userId      用户id
     * @param newPassword 新密码
     * @return 结果
     */
    Boolean sysResetUserPassword(Long userId, String newPassword);

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    Boolean deleteUserById(Long userId);

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    Boolean deleteUserByIds(List<Long> userIds);
}
