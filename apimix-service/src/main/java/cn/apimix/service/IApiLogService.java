package cn.apimix.service;

import cn.apimix.common.model.InterfaceLog;
import cn.apimix.model.entity.ApiLog;
import cn.apimix.model.vo.api.ApiStatistics;
import cn.apimix.model.vo.api.MonitorLine;
import com.mybatisflex.core.service.IService;

/**
 * @Author: Hor
 * @Date: 2024/8/10 下午1:32
 * @Version: 1.0
 */
public interface IApiLogService extends IService<ApiLog> {


    /**
     * 新增接口日志
     *
     * @param apiLog 日志
     * @return boolean
     */
    Boolean insertApiLog(InterfaceLog apiLog);


    /**
     * 根据请求唯一ID获取日志信息
     */
    ApiLog getApiLogByRequestId(String requestId);


    /**
     * 获取总日志统计
     * {总次数，总成功，总失败}
     */
    ApiStatistics getApiStatisticsByAll();


    //     * @param startDate 开始日期
    //     * @param endDate 结束日期
    MonitorLine getMonitorLine(Long apiId,Long userId, Long startTime, Long endTime);

    /**
     * 获取某用户日志统计
     * {次数，成功次，失败次}
     */

    /**
     * 获取某接口日志统计
     * {总次数，成功，失败}
     */

    /**
     * 根据用户和接口获取日志统计
     * {总次数，成功，失败}
     */

    /**
     * 获取某接口某天的日志统计
     * {总次数，成功，失败}
     */


}
