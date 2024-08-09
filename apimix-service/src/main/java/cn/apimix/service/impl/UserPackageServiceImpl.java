package cn.apimix.service.impl;

import cn.apimix.mapper.UserPackageMapper;
import cn.apimix.model.entity.UserPackage;
import cn.apimix.model.entity.table.UserPackageTableDef;
import cn.apimix.service.IUserPackageService;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.core.update.UpdateWrapper;
import com.mybatisflex.core.util.UpdateEntity;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务层实现。
 *
 * @author Hor
 * @since 2024-06-17
 */
@Service
public class UserPackageServiceImpl extends ServiceImpl<UserPackageMapper, UserPackage> implements IUserPackageService {


    /**
     * 获取套餐购买次数
     *
     * @param id 套餐ID
     * @param userId 用户ID
     */
    @Override
    public Long getPackageCount(Long id,Long userId) {
        return count(query().where(
                UserPackageTableDef.USER_PACKAGE.PACKAGE_ID.eq(id)).and(
                UserPackageTableDef.USER_PACKAGE.USER_ID.eq(userId))
        );
    }

    /**
     * 获取可用套餐
     *
     * @param apiId 接口
     * @param userId 用户
     */
    @Override
    public List<UserPackage> getAvailablePackages(Long apiId, Long userId) {
        return list(query().where(
                UserPackageTableDef.USER_PACKAGE.API_ID.eq(apiId)
                    .and(UserPackageTableDef.USER_PACKAGE.USER_ID.eq(userId))
                    .and(UserPackageTableDef.USER_PACKAGE.TOTAL_QUOTA.gt(UserPackageTableDef.USER_PACKAGE.USED_QUOTA))
                    .and(UserPackageTableDef.USER_PACKAGE.EXPIRED_TIME.gt(QueryMethods.now()))
            ) // 过期时间小到大，时间短的先用
                .orderBy(UserPackageTableDef.USER_PACKAGE.EXPIRED_TIME.asc())
                // 根据次数降序，用过的先用
                .orderBy(UserPackageTableDef.USER_PACKAGE.USED_QUOTA.desc())
                // 总次数升序，次数少的先用
                .orderBy(UserPackageTableDef.USER_PACKAGE.TOTAL_QUOTA.asc())
        );
    }

    /**
     * 增加套餐调用次数
     *
     * @param id 套餐id
     * @return 结果
     */
    @Override
    public Boolean increaseTheNumberOfCalls(Long id) {
        return UpdateChain.of(UserPackage.class)
                .setRaw(UserPackage::getUsedQuota, "used_quota + 1")
                .where(UserPackage::getId).eq(id)
                .update();
    }


}
