package cn.apimix.model.mapstruct;

import cn.apimix.model.dto.system.notice.SysNoticeAddRequest;
import cn.apimix.model.dto.system.notice.SysNoticeEditRequest;
import cn.apimix.model.entity.Notice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @Author: Hor
 * @Date: 2024/5/25 22:15
 * @Version: 1.0
 */
@Mapper(componentModel = "spring")
public interface NoticeMapping {

    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    Notice sysNoticeAddRequestToNotice(SysNoticeAddRequest notice);



    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    Notice sysNoticeEditRequestToNotice(SysNoticeEditRequest notice);

}
