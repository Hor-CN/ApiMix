package cn.apimix.controller;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.model.dto.api.MonitorLineRequest;
import cn.apimix.model.vo.api.ApiStatistics;
import cn.apimix.service.impl.ApiLogServiceImpl;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author: Hor
 * @Date: 2024/8/10 下午8:07
 * @Version: 1.0
 */
@RestController
@ResponseResult
@RequestMapping("/api/sentinel")
public class SentinelController {

    @Resource
    private ApiLogServiceImpl apiLogService;


    /**
     * 获取当接口统计
     */
    @SaCheckLogin
    @PostMapping("line")
    public Object getMonitorLine(@RequestBody MonitorLineRequest request) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        return apiLogService.getMonitorLine(request.getId(), currentUserId, request.getStartTime(), request.getEndTime());
    }


    /**
     * 获取用户某接口统计
     */
    @SaCheckLogin
    @PostMapping("statistics")
    public ApiStatistics getMonitorUser(@RequestBody MonitorLineRequest request) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        return apiLogService.getApiStatisticsByUser(request.getId(), currentUserId, request.getStartTime(), request.getEndTime());
    }


}
