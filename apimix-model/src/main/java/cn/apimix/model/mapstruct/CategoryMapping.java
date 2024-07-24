package cn.apimix.model.mapstruct;

import cn.apimix.model.dto.system.category.SysCategoryAddRequest;
import cn.apimix.model.dto.system.category.SysCategoryEditRequest;
import cn.apimix.model.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @Author: Hor
 * @Date: 2024/5/31 17:53
 * @Version: 1.0
 */
@Mapper(componentModel = "spring")
public interface CategoryMapping {

    @Mapping(target = "isDelete", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    Category sysCategoryAddRequestToCategory(SysCategoryAddRequest addRequest);



    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "isDelete", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    Category sysCategoryEditRequestToCategory(SysCategoryEditRequest editRequest);

}
