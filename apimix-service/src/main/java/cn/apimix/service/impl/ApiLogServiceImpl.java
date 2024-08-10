package cn.apimix.service.impl;

import cn.apimix.common.model.InterfaceLog;
import cn.apimix.mapper.ApiLogMapper;
import cn.apimix.model.entity.ApiLog;
import cn.apimix.model.entity.table.ApiLogTableDef;
import cn.apimix.model.mapstruct.ApiLogMapping;
import cn.apimix.model.vo.api.ApiStatistics;
import cn.apimix.service.IApiLogService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}
