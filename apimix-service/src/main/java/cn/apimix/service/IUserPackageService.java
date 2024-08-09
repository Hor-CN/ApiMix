package cn.apimix.service;

import cn.apimix.model.entity.UserPackage;
import com.mybatisflex.core.service.IService;

import java.util.List;


/**
 * 服务层。
 *
 * @author Hor
 * @since 2024-06-17
 */
public interface IUserPackageService extends IService<UserPackage> {

    /**
     * 获取套餐购买次数
     *
     * @param id 套餐ID
     * @param userId 用户ID
     */
    Long getPackageCount(Long id,Long userId);


    /**
     * 获取可用套餐
     */
    List<UserPackage> getAvailablePackages(Long apiId,Long userId);


    /**
     * 增加调用次数
     *
     * @param id 套餐id
     * @return 结果
     */
    Boolean increaseTheNumberOfCalls(Long id);

}
