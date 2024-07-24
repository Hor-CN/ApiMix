package cn.apimix.model.mapstruct;

import cn.apimix.model.dto.api.ApiAddRequest;
import cn.apimix.model.entity.ApiInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @Author: Hor
 * @Date: 2024/5/29 13:10
 * @Version: 1.0
 */
@Mapper(componentModel = "spring")
public interface ApiMapping {

    @Mapping(target = "returnType", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "isPaid", ignore = true)
    @Mapping(target = "isDelete", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "audit", ignore = true)
    ApiInfo apiAddRequestToApi(ApiAddRequest addRequest);


}
