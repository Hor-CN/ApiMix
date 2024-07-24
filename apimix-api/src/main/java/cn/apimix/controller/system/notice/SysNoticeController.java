package cn.apimix.controller.system.notice;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.core.core.model.IdsRequest;
import cn.apimix.model.dto.system.notice.SysNoticeAddRequest;
import cn.apimix.model.dto.system.notice.SysNoticeEditRequest;
import cn.apimix.model.dto.system.notice.SysNoticeQueryRequest;
import cn.apimix.model.entity.Notice;
import cn.apimix.model.mapstruct.NoticeMapping;
import cn.apimix.service.impl.NoticeServiceImpl;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Hor
 * @Date: 2024/5/25 22:09
 * @Version: 1.0
 */
@RestController
@ResponseResult
@RequestMapping("/api/system/notice")
public class SysNoticeController {

    @Resource
    private NoticeServiceImpl noticeService;

    @Resource
    private NoticeMapping noticeMapping;

    /**
     * 添加通知
     *
     * @return 结果
     */
    @SaCheckLogin
    @SaCheckPermission("sys:notice:add")
    @PostMapping("save")
    public Boolean add(@RequestBody @Valid SysNoticeAddRequest addRequest) {
        Notice notice = noticeMapping.sysNoticeAddRequestToNotice(addRequest);
        notice.setCreateBy(StpUtil.getLoginIdAsLong());
        return noticeService.insertNotice(notice);
    }

    /**
     * 修改通知
     */
    @SaCheckLogin
    @SaCheckPermission("sys:notice:edit")
    @PostMapping("edit")
    public Boolean edit(@RequestBody @Valid SysNoticeEditRequest editRequest) {
        Notice notice = noticeMapping.sysNoticeEditRequestToNotice(editRequest);
        return noticeService.updateNotice(notice);
    }

    /**
     * 删除通知
     */
    @SaCheckLogin
    @SaCheckPermission("sys:notice:del")
    @PostMapping("delete")
    public Boolean del(@RequestBody @Valid IdsRequest ids) {
        List<Long> longIds = ids.getIds().stream()
                .mapToLong(Integer::longValue)
                .boxed()
                .collect(Collectors.toList());
        return noticeService.deleteNoticeByIds(longIds);
    }

    @SaCheckLogin
    @SaCheckPermission("sys:notice:list")
    @GetMapping()
    public Page<Notice> pageList(@Valid SysNoticeQueryRequest queryRequest) {
        return noticeService.selectNoticeList(queryRequest);
    }

    /**
     * 根据通知编号获取详细信息
     */
    @SaCheckLogin
    @SaCheckPermission("system:notice:query")
    @GetMapping(value = "/{noticeId}")
    public Notice getInfo(@PathVariable Long noticeId) {
        return noticeService.selectNoticeById(noticeId);
    }


}
