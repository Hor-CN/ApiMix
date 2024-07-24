package cn.apimix.service;

import cn.apimix.model.entity.PackageType;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * 服务层。
 *
 * @author Hor
 * @since 2024-06-17
 */
public interface IPackageTypeService extends IService<PackageType> {


    /**
     * 新增套餐类型
     *
     * @param packageName 套餐类型名称
     * @return 结果
     */
    Boolean addPackageType(String packageName);


    /**
     * 删除套餐类型
     *
     * @param id 套餐ID
     * @return 结果
     */
    Boolean deletePackageType(Integer id);

    /**
     * 修改套餐名称
     *
     * @param packageName 套餐名称
     * @return 结果
     */
    Boolean updatePackageType(Integer id, String packageName);


    /**
     * 获取套餐类型信息
     *
     * @param id 套餐ID
     * @return 结果
     */
    PackageType getPackageTypeById(Integer id);


    /**
     * 获取所有的套餐类型
     */
    List<PackageType> getPackageTypeList();

}
