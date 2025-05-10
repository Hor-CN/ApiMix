package cn.apimix.user.service;


import cn.apimix.user.model.entity.User;
import cn.apimix.user.model.req.UserEditRequest;
import cn.apimix.user.model.req.system.SysUserAddRequest;
import cn.apimix.user.model.req.system.SysUserEditRequest;
import cn.apimix.user.model.req.system.SysUserQueryRequest;
import cn.apimix.user.model.resp.user.UserInfoResp;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 用户服务层。
 *
 * @Author: Hor
 * @Date: 2024/5/21 14:50
 * @Version: 1.0
 */
public interface UserService extends IService<User> {


    String updateAvatar(MultipartFile avatarFile, Long userId);

    String existsUserGenUserName(String username);

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    Page<User> selectUserByPage(SysUserQueryRequest user);

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserInfoResp getUserInfo(Long userId);

    /**
     * 将User 转 UserInfo
     * @param user 用户
     * @return 用户信息
     */
    UserInfoResp getUserToUserInfo(User user);

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

    Boolean updateEmail(String newEmail, String oldPassword, Long id);


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
    Boolean updateBasicInfo(UserEditRequest editRequest, Long userId);

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
     */
    void resetUserPassword(Long userId, String oldPassword, String newPassword);

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
