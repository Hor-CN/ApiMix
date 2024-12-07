package cn.apimix.controller.console;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.core.core.model.IdRequest;
import cn.apimix.core.core.model.PageRequest;
import cn.apimix.model.dto.system.notice.SysNoticeQueryRequest;
import cn.apimix.model.entity.Notice;
import cn.apimix.model.vo.api.ApiRelationVo;
import cn.apimix.model.vo.api.ApiVo;
import cn.apimix.model.vo.console.home.InfoVo;
import cn.apimix.model.vo.console.home.StatisticVo;
import cn.apimix.model.vo.console.home.Xy;
import cn.apimix.service.impl.*;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.paginate.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 控制台首页 console/home
 *
 * @Author: Hor
 * @Date: 2024/11/23 20:44
 * @Version: 1.0
 */
@RestController
@ResponseResult
@RequiredArgsConstructor
@RequestMapping("/api/console/home")
public class HomeController {

    private final ApiServiceImpl apiService;

    private final UserApiRelationServiceImpl relationService;

    private final AccountServiceImpl accountService;

    private final UserPackageServiceImpl userPackageService;

    private final NoticeServiceImpl noticeService;


    private final ApiLogServiceImpl apiLogService;



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
    public List<Xy> getReport() {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        return apiLogService.getCountByUserIdAndYearMonth(currentUserId);
    }




}
