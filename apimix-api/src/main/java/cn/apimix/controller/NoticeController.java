package cn.apimix.controller;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.model.dto.system.notice.SysNoticeQueryRequest;
import cn.apimix.model.entity.Notice;
import cn.apimix.service.impl.NoticeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 通知公告
 *
 * @Author: Hor
 * @Date: 2024/11/23 16:51
 * @Version: 1.0
 */
@RestController
@ResponseResult
@RequestMapping("/api/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeServiceImpl noticeService;


    @GetMapping()
    public List<Notice> getNotices() {
        SysNoticeQueryRequest queryRequest = SysNoticeQueryRequest.builder()
                .status(true)
                .build();
        queryRequest.setPageSize(5L);
        queryRequest.setPageNumber(1L);
        return noticeService.selectNoticeList(queryRequest).getRecords();
    }

    @GetMapping(value = "/{noticeId}")
    public Notice getInfo(@PathVariable Long noticeId) {
        return noticeService.getById(noticeId);
    }


}
