package cn.apimix.service;

import cn.apimix.model.dto.system.notice.SysNoticeQueryRequest;
import cn.apimix.model.entity.Notice;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/25 19:47
 * @Version: 1.0
 */
public interface INoticeService extends IService<Notice> {
    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    Notice selectNoticeById(Long noticeId);

    /**
     * 查询公告列表
     *
     * @param queryRequest 公告信息查询
     * @return 公告集合
     */
    Page<Notice> selectNoticeList(SysNoticeQueryRequest queryRequest);


    /**
     * 新增公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    Boolean insertNotice(Notice notice);


    /**
     * 修改公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    Boolean updateNotice(Notice notice);


    /**
     * 删除公告信息
     *
     * @param noticeId 公告ID
     * @return 结果
     */
    Boolean deleteNoticeById(Long noticeId);

    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    Boolean deleteNoticeByIds(List<Long> noticeIds);
}
