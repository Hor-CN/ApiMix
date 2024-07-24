package cn.apimix.service;

import cn.apimix.model.entity.Package;
import cn.apimix.model.vo.api.SkuVo;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * 服务层。
 *
 * @author Hor
 * @since 2024-06-17
 */
public interface IPackageService extends IService<Package> {


    /**
     * 根据接口ID获取套餐列表和套餐类型
     *
     * @param id 接口ID
     */
    List<SkuVo> getSkuList(Long id);


    /**
     * 查询所有的套餐
     *
     * @param apiId       接口ID
     * @param packageType 套餐类型
     * @return 套餐列表
     */
    List<Package> selectAllPackage(Long apiId, Long packageType);

    /**
     * 获取套餐详情
     */
    Package selectPackage(Long id);

    /**
     * 新增套餐
     */
    Boolean addPackage(Package pkg);


    /**
     * 删除套餐
     */
    Boolean deletePackage(Long id);


    /**
     * 购买套餐
     */
    Boolean purchasePackage(Long packageId, Long userId, Integer count);
}
