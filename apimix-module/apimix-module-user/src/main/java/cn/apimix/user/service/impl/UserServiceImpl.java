package cn.apimix.user.service.impl;

import cn.apimix.core.constant.StringConstants;
import cn.apimix.core.model.PageRequest;
import cn.apimix.model.entity.UserAccount;
import cn.apimix.user.mapper.UserMapper;
import cn.apimix.user.model.entity.Role;
import cn.apimix.user.model.entity.User;
import cn.apimix.user.model.entity.table.UserTableDef;
import cn.apimix.user.model.mapstruct.UserMapping;
import cn.apimix.user.model.req.UserEditRequest;
import cn.apimix.user.model.req.system.SysUserAddRequest;
import cn.apimix.user.model.req.system.SysUserEditRequest;
import cn.apimix.user.model.req.system.SysUserQueryRequest;
import cn.apimix.user.model.resp.user.UserInfoResp;
import cn.apimix.user.service.UserAccountService;
import cn.apimix.user.service.UserService;
import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.If;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.StringUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
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
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Resource
    private UserRoleServiceImpl userRoleService;

    @Resource
    private UserAccountService userAccountService;

    @Resource
    private UserMapping userMapping;

    @Value("${avatar.support-suffix}")
    private String[] avatarSupportSuffix;


    @Override
    public String existsUserGenUserName(String username) {
        if (exists(query().where(UserTableDef.USER.USER_NAME.eq(username)))) {
            int randomInt = RandomUtil.randomInt(0, 100);
            return existsUserGenUserName(username + randomInt);
        }
        return username;
    }


    /**
     * 更新头像
     */
    @SneakyThrows
    @Override
    public String updateAvatar(MultipartFile avatarFile, Long id) {
        String avatarImageType = FileNameUtil.extName(avatarFile.getOriginalFilename());

        Assert.isFalse(!StrUtil.equalsAnyIgnoreCase(avatarImageType, avatarSupportSuffix), "头像仅支持 {} 格式的图片", String
                .join(StringConstants.CHINESE_COMMA, avatarSupportSuffix));

        // 更新用户头像
        String base64 = ImgUtil.toBase64DataUri(ImgUtil.scale(ImgUtil.toImage(avatarFile
                .getBytes()), 100, 100, null), avatarImageType);

        updateById(User.builder()
                .id(id)
                .avatar(base64)
                .build());
        return base64;
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
                .where(UserTableDef.USER.USER_NAME.like(user.getUserName(), StringUtil::isNotBlank)
                        .and(UserTableDef.USER.STATUS.eq(user.getStatus(), If::notNull)));
        return mapper.paginateWithRelations(page, queryWrapper);
    }

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    @Override
    public UserInfoResp getUserInfo(Long userId) {
        User user = selectUserById(userId);
        return getUserToUserInfo(user);
    }

    /**
     * 将User 转 UserInfo
     *
     * @param user 用户
     * @return 用户信息
     */
    @Override
    public UserInfoResp getUserToUserInfo(User user) {
        UserInfoResp userInfoResp = userMapping.userToUserVo(user);
        userInfoResp.setType(null);
        userInfoResp.setPhone(null);
        List<Integer> roleIds = user.getRoles().stream().map(Role::getId).collect(Collectors.toList());
        userInfoResp.setRoleIds(roleIds);
        List<String> roleNames = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        userInfoResp.setRoleNames(roleNames);
        return userInfoResp;
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
        List<Integer> roleIds = addRequest.getRoleIds();

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
        // 创建用户账户信息
        userAccountService.save(UserAccount.builder()
                .userId(user.getId())
                .build());
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
        List<Integer> roleIds = editRequest.getRoleIds();
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
    public Boolean updateBasicInfo(UserEditRequest editRequest, Long userId) {
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
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetUserPassword(Long userId, String oldPassword, String newPassword) {
        User user = getById(userId);
        // 比对新旧密码
        String salt = user.getSalt();
        String oldPasswordSalt = SaSecureUtil.md5BySalt(oldPassword, salt);
        String newPasswordSalt = SaSecureUtil.md5BySalt(newPassword, salt);
        Assert.isFalse(oldPasswordSalt.equals(newPasswordSalt), "原密码不正确");
        user.setPassword(newPasswordSalt);
        updateById(user);
    }

    @Override
    public Boolean updateEmail(String newEmail, String oldPassword, Long id) {
        User user = getById(id);
        String oldPasswordSalt = SaSecureUtil.md5BySalt(oldPassword, user.getSalt());

        Assert.isTrue(oldPasswordSalt.equals(user.getPassword()), "原密码不正确");

        boolean exists = queryChain().where(UserTableDef.USER.EMAIL.eq(newEmail)).exists();
        Assert.isFalse(exists, "邮箱已绑定其他账号，请更换其他邮箱");
        Assert.notEquals(newEmail, user.getEmail(), "新邮箱不能与当前邮箱相同");
        // 更新邮箱
        user.setEmail(newEmail);
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


    /**
     * 分页获取待审核的用户认证列表
     */
    public Page<User> selectAuditUserDevByPage(PageRequest request) {
//        QueryWrapper queryWrapper = QueryWrapper.create()
//                .select(UserTableDef.USER.ALL_COLUMNS)
//                .leftJoin(AuditTableDef.AUDIT)
//                .on(UserTableDef.USER.ID.eq(AuditTableDef.AUDIT.FLOW_NO))
//                .where(AuditTableDef.AUDIT.STATUS.eq(1));
//        return page(Page.of(request.getPageNumber(), request.getPageSize()),
//                queryWrapper);
        return null;
    }






}
