package cn.apimix.controller.console;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.model.vo.api.ApiStatistics;
import cn.apimix.model.vo.console.analyse.ActiveVo;
import cn.apimix.model.vo.console.ChartDataVo;
import cn.apimix.model.vo.console.analyse.IpVo;
import cn.apimix.model.vo.console.analyse.MethodVo;
import cn.apimix.model.vo.console.analyse.PvVo;
import cn.apimix.model.vo.console.home.*;
import cn.apimix.service.impl.ApiLogServiceImpl;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/11/25 15:06
 * @Version: 1.0
 */
@RestController
@ResponseResult
@RequiredArgsConstructor
@RequestMapping("/api/console/analysis")
public class AnalysisController {

    private final ApiLogServiceImpl apiLogService;

    @SaCheckLogin
    @GetMapping("pv")
    public PvVo getPv() {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        List<Xy> dataList = apiLogService.getCountByUserIdAndYearMonth(currentUserId);

        ApiStatistics apiStatistics = apiLogService.getApiStatisticsByUserId(currentUserId);
        Long countByToday = apiLogService.getCountByToday(currentUserId);
        Long countByYesterday = apiLogService.getCountByYesterday(currentUserId);

        return PvVo.builder()
                .dataList(dataList)
                .total(apiStatistics.getTotalNumber())
                .today(countByToday)
                .yesterday(countByYesterday)
                .build();
    }

    @SaCheckLogin
    @GetMapping("ip")
    public IpVo getIp() {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        List<Xy> dataList = apiLogService.getCountIpByUserId(currentUserId);

        Long countIpByTotal = apiLogService.getCountIpByTotal(currentUserId);
        Long countByToday = apiLogService.getCountIpByToday(currentUserId);
        Long countByYesterday = apiLogService.getCountIpByYesterday(currentUserId);

        return IpVo.builder()
                .dataList(dataList)
                .total(countIpByTotal)
                .today(countByToday)
                .yesterday(countByYesterday)
                .build();
    }


    @SaCheckLogin
    @GetMapping("geo")
    public List<ChartDataVo> getGeo() {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        return apiLogService.getCountAddressByUserId(currentUserId);
    }

    @SaCheckLogin
    @GetMapping("active")
    public ActiveVo getActiveVo() {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        List<ChartDataVo> activeApis = apiLogService.getActiveApiByUserId(currentUserId);

        Long total = apiLogService.getCountActiveApiByTotal(currentUserId);
        Long today = apiLogService.getCountActiveApiByToday(currentUserId);
        Long yesterday = apiLogService.getCountActiveApiByYesterday(currentUserId);

        return ActiveVo.builder()
                .dataList(activeApis)
                .total(total)
                .today(today)
                .yesterday(yesterday)
                .build();
    }

    @SaCheckLogin
    @GetMapping("hit")
    public ApiStatistics getApiStatistics() {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        return apiLogService.getApiStatisticsByUserId(currentUserId);
    }


    @SaCheckLogin
    @GetMapping("method")
    public MethodVo getMethodVo() {
        Long currentUserId = StpUtil.getLoginIdAsLong();

        Long gets = apiLogService.getCountMethodGetByUserId(currentUserId);
        Long puts = apiLogService.getCountMethodPutByUserId(currentUserId);
        Long post = apiLogService.getCountMethodPostByUserId(currentUserId);
        Long deletes = apiLogService.getCountMethodDelByUserId(currentUserId);
        Long others = apiLogService.getCountMethodOtherByUserId(currentUserId);

        return MethodVo.builder()
                .get(gets)
                .put(puts)
                .post(post)
                .delete(deletes)
                .other(others)
                .build();
    }


    @SaCheckLogin
    @GetMapping("hot")
    public List<ChartDataVo> getHot() {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        return apiLogService.getHotCountByUserId(currentUserId,5);
    }




}
