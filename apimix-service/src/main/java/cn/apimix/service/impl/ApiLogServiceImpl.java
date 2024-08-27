package cn.apimix.service.impl;

import cn.apimix.common.model.InterfaceLog;
import cn.apimix.mapper.ApiLogMapper;
import cn.apimix.model.entity.ApiLog;
import cn.apimix.model.entity.table.ApiLogTableDef;
import cn.apimix.model.mapstruct.ApiLogMapping;
import cn.apimix.model.vo.api.ApiStatistics;
import cn.apimix.model.vo.api.MonitorItem;
import cn.apimix.model.vo.api.MonitorLine;
import cn.apimix.service.IApiLogService;
import cn.hutool.core.date.DateTime;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Hor
 * @Date: 2024/8/10 下午1:32
 * @Version: 1.0
 */
@Service
public class ApiLogServiceImpl extends ServiceImpl<ApiLogMapper, ApiLog> implements IApiLogService {


    @Resource
    private ApiLogMapping apiLogMapping;

    /**
     * 新增接口日志
     *
     * @param apiLog 日志
     * @return boolean
     */
    @Override
    public Boolean insertApiLog(InterfaceLog apiLog) {
        return save(apiLogMapping.interfaceLogToApiLog(apiLog));
    }

    /**
     * 根据请求唯一ID获取日志信息
     *
     * @param requestId 请求id
     */
    @Override
    public ApiLog getApiLogByRequestId(String requestId) {
        return getOne(query().where(ApiLogTableDef.API_LOG.REQUEST_ID.eq(requestId)));
    }

    /**
     * 获取总日志统计
     * {总次数，总成功，总失败}
     */
    @Override
    public ApiStatistics getApiStatisticsByAll() {
        Long totalNumber = count();
        Long successNumber = count(query().where(ApiLogTableDef.API_LOG.STATUS.eq("成功")));
        return ApiStatistics.builder().totalNumber(totalNumber).successNumber(successNumber).failedNumber(totalNumber - successNumber).build();
    }


    public ApiStatistics getApiStatisticsByUser(Long apiId, Long userId, Long startTime, Long endTime) {
        Long totalNumber = count(query()
                .where(ApiLogTableDef.API_LOG.TARGET_SERVER.eq(apiId))
                .and(ApiLogTableDef.API_LOG.USER_ID.eq(userId))
                .and(ApiLogTableDef.API_LOG.TARGET_SERVER.eq(apiId)));
        Long successNumber = count(query()
                .where(ApiLogTableDef.API_LOG.TARGET_SERVER.eq(apiId))
                .and(ApiLogTableDef.API_LOG.STATUS.eq("成功"))
                .and(ApiLogTableDef.API_LOG.USER_ID.eq(userId)));
        return ApiStatistics.builder().totalNumber(totalNumber).successNumber(successNumber).failedNumber(totalNumber - successNumber).build();
    }

    @Override
    public MonitorLine getMonitorLine(Long apiId, Long userId, Long startTime, Long endTime) {
        List<ApiLog> apiLogs = list(query()
                .where(ApiLogTableDef.API_LOG.TARGET_SERVER.eq(apiId))
                .and(ApiLogTableDef.API_LOG.USER_ID.eq(userId))
                .and(ApiLogTableDef.API_LOG.END_TIME.gt(DateTime.of(startTime)))
                .and(ApiLogTableDef.API_LOG.END_TIME.lt(DateTime.of(endTime)))
                .orderBy(ApiLogTableDef.API_LOG.END_TIME.asc())
        );
        Map<String, Long> countMap = apiLogs.stream()
                .collect(Collectors.groupingBy(item -> new SimpleDateFormat("yyyy-MM-dd").format(item.getEndTime()), Collectors.counting()));

        List<MonitorItem> itemList = countMap.entrySet().stream()
                .map(entry -> new MonitorItem(entry.getKey(), entry.getValue().toString()))
                .collect(Collectors.toList());

        if (itemList.size() < 7) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            Date currentDate = new Date();
            calendar.setTime(currentDate);
            for (int i = itemList.size(); i <= 7; i++) {
                if (!countMap.containsKey(sdf.format(calendar.getTime()))) {
                    Collections.addAll(itemList, MonitorItem.builder()
                            .time(sdf.format(calendar.getTime()))
                            .value("0")
                            .build());
                }
                calendar.add(Calendar.DAY_OF_MONTH, -1);
            }
        }
        itemList.sort(Comparator.comparing(MonitorItem::getTime));

        return MonitorLine.builder()
                .sum(apiLogs.stream().count())
                .items(itemList)
                .build();
    }
}
