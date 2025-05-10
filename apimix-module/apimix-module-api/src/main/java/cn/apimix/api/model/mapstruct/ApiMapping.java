package cn.apimix.api.model.mapstruct;

import cn.apimix.api.model.entity.ApiVersion;
import cn.apimix.api.model.req.ApiAddRequest;
import cn.apimix.api.model.req.ApiEditRequest;
import cn.apimix.api.model.resp.ApiReleaseResp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @Author: Hor
 * @Date: 2025/2/18 16:35
 * @Version: 1.0
 */
@Mapper(componentModel = "spring")
public interface ApiMapping {


    @Mapping(target = "returnType", ignore = true)
//    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "isPaid", ignore = true)
    @Mapping(target = "isDelete", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
//    @Mapping(target = "updateTime", ignore = true)
//    @Mapping(target = "audit", ignore = true)
    ApiVersion apiAddRequestToApiVersion(ApiAddRequest apiAddRequest);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "id",source = "apiId")
    ApiReleaseResp apiVersionToApiReleaseResp(ApiVersion apiVersion);


    @Mapping(target = "apiId", source = "id")
    ApiVersion apiEditRequestToApiVersion(ApiEditRequest apiEditRequest);

}
