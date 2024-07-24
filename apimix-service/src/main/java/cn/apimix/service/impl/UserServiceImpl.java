package cn.apimix.service.impl;

import cn.apimix.mapper.UserMapper;
import cn.apimix.model.dto.system.user.SysUserAddRequest;
import cn.apimix.model.dto.system.user.SysUserEditRequest;
import cn.apimix.model.dto.system.user.SysUserQueryRequest;
import cn.apimix.model.dto.user.UserEditRequest;
import cn.apimix.model.dto.user.UserLoginRequest;
import cn.apimix.model.entity.Role;
import cn.apimix.model.entity.User;
import cn.apimix.model.entity.table.UserTableDef;
import cn.apimix.model.enums.RoleTypeEnum;
import cn.apimix.model.mapstruct.UserMapping;
import cn.apimix.service.IUserService;
import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.If;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.StringUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户
 *
 * @Author: Hor
 * @Date: 2024/5/21 18:38
 * @Version: 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private RoleServiceImpl roleService;

    @Resource
    private UserRoleServiceImpl userRoleService;

    @Resource
    private UserMapping userMapping;

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求信息
     * @return 用户ID
     */
    @Override
    public Long login(UserLoginRequest loginRequest) {
        // 用户不存在
        boolean exists = queryChain().where(UserTableDef.USER.USER_NAME.eq(loginRequest.getUsername())).exists();
        Assert.isTrue(exists, "账号或密码错误，登录失败");
        // 获取用户
        User user = queryChain().where(UserTableDef.USER.USER_NAME.eq(loginRequest.getUsername())).one();
        // 判断密码是否相等
        Assert.equals(user.getPassword(),
                SaSecureUtil.md5BySalt(loginRequest.getPassword(), user.getSalt()),
                "账号或密码错误，登录失败"
        );
        // 判断用户状态是否处于禁用状态
        if (user.getStatus() == 0) {
            StpUtil.disable(user.getId(), -1);
        } else if (StpUtil.isDisable(user.getId())) {
            StpUtil.untieDisable(user.getId());
        }
        Assert.isFalse(
                StpUtil.isDisable(user.getId()),
                "账号已被封禁" + StpUtil.getDisableTime(user.getId())
        );
        // 登录成功
        SaLoginModel saLoginModel = SaLoginModel.create()
                .setDevice(loginRequest.getDevice())
                .setIsLastingCookie(loginRequest.getChecked())
                .build();
        StpUtil.login(user.getId(), saLoginModel);
        // 存在 Account-Session 中 减少查找数据库
        StpUtil.getSession().set("user", user);
        // 返回用户ID
        return user.getId();
    }

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public Page<User> selectUserByPage(SysUserQueryRequest user) {
        // 1. 构建分页
        Page<User> page = Page.of(user.getPageNumber(), user.getPageSize());
        // 2. 构建查询
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(UserTableDef.USER.USER_NAME.like(user.getUsername(), StringUtil::isNotBlank)
                        .and(UserTableDef.USER.STATUS.eq(user.getStatus(), If::notNull)));
        return mapper.paginateWithRelations(page, queryWrapper);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public User selectUserById(Long userId) {
        // 校验用户是否存在
        boolean exists = queryChain().where(UserTableDef.USER.ID.eq(userId)).exists();
        Assert.isTrue(exists, "用户不存在");
        return mapper.selectOneWithRelationsById(userId);
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public User selectUserByUserName(String userName) {
        // 构建查询
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(UserTableDef.USER.USER_NAME.eq(userName));
        return mapper.selectOneByQuery(queryWrapper);
    }

    /**
     * 新增保存用户信息
     *
     * @param addRequest 用户信息
     * @return 结果
     */
    @Override
    public Boolean insertUser(SysUserAddRequest addRequest) {
        // 校验用户是否存在
        boolean exists = queryChain().where(UserTableDef.USER.USER_NAME.eq(addRequest.getUsername())).exists();
        Assert.isFalse(exists, "用户已存在");
        // 获取分配的角色
        List<Role> roles = roleService.selectRoleByRoleCode(addRequest.getRoles());
        // 使用Stream过滤内置用户角色
        List<Integer> roleIds = roles.stream()
                .filter(role -> role.getType() != RoleTypeEnum.INTERNAL.getType())
                .map(Role::getId)
                .collect(Collectors.toList());

        Assert.notEmpty(roleIds, "必须分配至少一个角色");

        // 每个用户创建一个独立的密码加密盐值
        String salt = SecureUtil.md5(addRequest.getPassword() + Arrays.toString(RandomUtil.randomBytes(6)));
        // 构建用户实体
        User user = User.builder()
                .userName(addRequest.getUsername())
                // MD5( MD5(密码) + MD5(盐值) )
                .password(SaSecureUtil.md5BySalt(addRequest.getPassword(), salt))
                // 设置盐值
                .salt(salt)
                .nickName(addRequest.getNickname())
                .description(addRequest.getDescription())
                .status(addRequest.getStatus())
                .email(addRequest.getEmail())
                .phone(addRequest.getPhone())
                .gender(addRequest.getGender())
                .build();
        // 是否创建用户成功
        boolean isSave = save(user);
        // 新增用户与角色关联
        userRoleService.insertBatchUserRole(user.getId(), roleIds);
        return isSave;
    }

    /**
     * 修改保存用户信息
     *
     * @param editRequest 用户信息
     * @return 结果
     */
    @Override
    public Boolean updateUser(SysUserEditRequest editRequest) {
        // 校验用户是否存在
        boolean exists = queryChain().where(UserTableDef.USER.ID.eq(editRequest.getId())).exists();
        Assert.isTrue(exists, "用户不存在");
        // 构建用户实体
        User user = userMapping.sysUserEditRequestToUser(editRequest);
        // 删除用户与角色的关联
        userRoleService.deleteUserRole(editRequest.getId());
        // 获取分配的角色ID
        List<Integer> roleIds = roleService.selectRoleIdByRoleCode(editRequest.getRoles());
        // 新增用户与角色关联
        userRoleService.insertBatchUserRole(editRequest.getId(), roleIds);
        return updateById(user);
    }

    /**
     * 修改用户信息
     *
     * @param editRequest 修改信息
     */
    @Override
    public Boolean updateProfile(UserEditRequest editRequest, Long userId) {
        User user = User.builder()
                .id(userId)
                .nickName(editRequest.getNickname())
                .gender(editRequest.getGender())
                .description(editRequest.getDescription())
                .build();
        return updateById(user);
    }


    /**
     * 重置用户密码
     *
     * @param user 用户
     * @return 结果
     */
    @Override
    public Boolean resetPassword(User user) {
        return updateById(user);
    }

    /**
     * 重置用户密码
     *
     * @param userId      用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 结果
     */
    @Override
    public Boolean resetUserPassword(Long userId, String oldPassword, String newPassword) {
        // 获取用户
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(UserTableDef.USER.ID.eq(userId));
        User user = getById(queryWrapper);
        // 比对新旧密码
        String salt = user.getSalt();
        String oldPasswordSalt = SaSecureUtil.md5BySalt(oldPassword, salt);
        String newPasswordSalt = SaSecureUtil.md5BySalt(newPassword, salt);
        Assert.isFalse(oldPasswordSalt.equals(newPasswordSalt), "原密码不正确");
        user.setPassword(newPasswordSalt);
        return updateById(user);
    }

    /**
     * 重置用户密码
     *
     * @param userId      用户id
     * @param newPassword 新密码
     * @return 结果
     */
    @Override
    public Boolean sysResetUserPassword(Long userId, String newPassword) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(UserTableDef.USER.ID.eq(userId));
        // 1. 获取该用户的salt
        String salt = getById(userId).getSalt();
        // 2. 加密密码 MD5( MD5(密码) + MD5(盐值) )
        String ps = SaSecureUtil.md5BySalt(newPassword, salt);
        // 3. 修改密码
        User user = User.builder()
                .password(ps)
                .build();
        return update(user, queryWrapper);
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public Boolean deleteUserById(Long userId) {
        return null;
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    public Boolean deleteUserByIds(List<Long> userIds) {
        if (userIds.isEmpty()) {
            return false;
        }
        // 判断删除用户中有没有当前用户，有则当前用户不能删除，可以放在controller中
        long loginIdAsLong = StpUtil.getLoginIdAsLong();
        Assert.isFalse(ArrayUtil.contains(userIds.toArray(), loginIdAsLong), "当前用户不能删除");
        // 逻辑删除该用户
        return removeByIds(userIds);
    }
}
