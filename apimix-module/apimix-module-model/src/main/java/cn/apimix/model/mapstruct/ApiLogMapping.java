package cn.apimix.model.mapstruct;

import cn.apimix.common.model.InterfaceLog;
import cn.apimix.model.entity.ApiLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @Author: Hor
 * @Date: 2024/8/10 下午4:37
 * @Version: 1.0
 */
@Mapper(componentModel = "spring")
public interface ApiLogMapping {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    ApiLog interfaceLogToApiLog(InterfaceLog interfaceLog);


}
