package cn.apimix.service;

import cn.apimix.model.entity.UserPackage;
import com.mybatisflex.core.service.IService;


/**
 * 服务层。
 *
 * @author Hor
 * @since 2024-06-17
 */
public interface IUserPackageService extends IService<UserPackage> {

    /**
     * 获取套餐购买次数
     */
    Long getPackageCount(Long id);

}
