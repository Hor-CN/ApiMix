package cn.apimix.service.impl;

import cn.apimix.mapper.PackageTypeMapper;
import cn.apimix.model.entity.PackageType;
import cn.apimix.service.IPackageTypeService;
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
public class PackageTypeServiceImpl extends ServiceImpl<PackageTypeMapper, PackageType> implements IPackageTypeService {

    /**
     * 新增套餐类型
     *
     * @param packageName 套餐类型名称
     * @return 结果
     */
    @Override
    public Boolean addPackageType(String packageName) {
        return save(PackageType.builder()
                .name(packageName)
                .build());
    }

    /**
     * 删除套餐类型
     *
     * @param id 套餐ID
     * @return 结果
     */
    @Override
    public Boolean deletePackageType(Integer id) {
        return removeById(id);
    }

    /**
     * 修改套餐名称
     *
     * @param packageName 套餐名称
     * @return 结果
     */
    @Override
    public Boolean updatePackageType(Integer id, String packageName) {
        return updateById(PackageType.builder()
                .id(id)
                .name(packageName)
                .build());
    }

    /**
     * 获取套餐类型信息
     *
     * @param id 套餐ID
     * @return 结果
     */
    @Override
    public PackageType getPackageTypeById(Integer id) {
        return getById(id);
    }

    /**
     * 获取所有的套餐类型
     */
    @Override
    public List<PackageType> getPackageTypeList() {
        return list();
    }


}
