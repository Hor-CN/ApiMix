package cn.apimix.service.impl;

import cn.apimix.mapper.UserPackageMapper;
import cn.apimix.model.entity.UserPackage;
import cn.apimix.model.entity.table.UserPackageTableDef;
import cn.apimix.service.IUserPackageService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
     */
    @Override
    public Long getPackageCount(Long id) {
        return count(query().where(UserPackageTableDef.USER_PACKAGE.PACKAGE_ID.eq(id)));
    }


    /**
     * 获取购买的套餐列表
     */





}
