package cn.apimix.controller.console;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.model.vo.api.ApiStatistics;
import cn.apimix.model.vo.api.ApiVo;
import cn.apimix.model.vo.console.ChartDataVo;
import cn.apimix.model.vo.console.analyse.ActiveVo;
import cn.apimix.model.vo.console.analyse.IpVo;
import cn.apimix.model.vo.console.analyse.MethodVo;
import cn.apimix.model.vo.console.analyse.PvVo;
import cn.apimix.model.vo.console.home.StatisticVo;
import cn.apimix.service.impl.UserAccountServiceImpl;
import cn.apimix.service.impl.ApiLogServiceImpl;
import cn.apimix.service.impl.ApiServiceImpl;
import cn.apimix.service.impl.UserApiRelationServiceImpl;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 仪表盘控制器
 *
 * @Author: Hor
 * @Date: 2024/12/21 15:47
 * @Version: 1.0
 */
@RestController
@ResponseResult
@RequiredArgsConstructor
@RequestMapping("/api/console/dashboard")
public class DashboardController {


    private final ApiLogServiceImpl apiLogService;

    private final ApiServiceImpl apiService;

    private final UserApiRelationServiceImpl relationService;

    private final UserAccountServiceImpl accountService;


    /**
     * 获取当前开发者贡献的接口
     */
    @SaCheckLogin
    @GetMapping("statistic")
    public StatisticVo getUserDevApiByCount() {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        return StatisticVo.builder()
                .amount(accountService.getAmount(currentUserId).getAmount())
                .applied(relationService.selectUserApiRelationByCount(currentUserId))
                .contribute(apiService.selectApiBycCount(currentUserId))
                .build();
    }


    /**
     * 获取当前开发者接口
     */
    @SaCheckLogin
    @GetMapping("reportApis")
    public List<ApiVo> getReportApis() {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        return relationService.selectUserApiRelationByList(currentUserId);
    }


    @SaCheckLogin
    @GetMapping("report")
    public List<ChartDataVo> getReport() {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        return apiLogService.getCountByUserIdAndYearMonth(currentUserId);
    }

    @SaCheckLogin
    @GetMapping("pv")
    public PvVo getPv() {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        List<ChartDataVo> dataList = apiLogService.getCountByUserIdAndYearMonth(currentUserId);

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
        List<ChartDataVo> dataList = apiLogService.getCountIpByUserId(currentUserId);

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
