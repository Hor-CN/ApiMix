package cn.apimix.service.impl;

import cn.apimix.mapper.NoticeMapper;
import cn.apimix.model.dto.system.notice.SysNoticeQueryRequest;
import cn.apimix.model.entity.Notice;
import cn.apimix.model.entity.table.NoticeTableDef;
import cn.apimix.service.INoticeService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.If;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.StringUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/5/25 19:50
 * @Version: 1.0
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    @Override
    public Notice selectNoticeById(Long noticeId) {
        return getById(noticeId);
    }

    /**
     * 查询公告列表
     *
     * @param queryRequest 公告信息
     * @return 公告集合
     */
    @Override
    public Page<Notice> selectNoticeList(SysNoticeQueryRequest queryRequest) {
        Page<Notice> page = Page.of(queryRequest.getPageNumber(), queryRequest.getPageSize());
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(NoticeTableDef.NOTICE.TITLE.like(queryRequest.getTitle(), StringUtil::isNotBlank))
                .and(NoticeTableDef.NOTICE.TYPE.eq(queryRequest.getType(), If::notNull))
                .and(NoticeTableDef.NOTICE.STATUS.eq(queryRequest.getStatus(), If::notNull));
        return page(page, queryWrapper);
    }

    /**
     * 新增公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public Boolean insertNotice(Notice notice) {
        return save(notice);
    }

    /**
     * 修改公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public Boolean updateNotice(Notice notice) {
        return updateById(notice);
    }

    /**
     * 删除公告信息
     *
     * @param noticeId 公告ID
     * @return 结果
     */
    @Override
    public Boolean deleteNoticeById(Long noticeId) {
        return removeById(noticeId);
    }

    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    @Override
    public Boolean deleteNoticeByIds(List<Long> noticeIds) {
        return removeByIds(noticeIds);
    }
}
