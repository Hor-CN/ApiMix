package cn.apimix.controller.console;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.core.model.PageRequest;
import cn.apimix.model.dto.api.ApiInfoQueryRequest;
import cn.apimix.model.dto.api.MonitorLineRequest;
import cn.apimix.model.dto.token.AllocationTokenAddRequest;
import cn.apimix.model.dto.token.AllocationTokenEditRequest;
import cn.apimix.model.entity.ApiToken;
import cn.apimix.model.entity.UserPackage;
import cn.apimix.model.vo.api.AllocationTokenVO;
import cn.apimix.model.vo.api.ApiRelationVo;
import cn.apimix.model.vo.api.ApiStatistics;
import cn.apimix.model.vo.console.ChartDataVo;
import cn.apimix.service.ApiTokenService;
import cn.apimix.service.impl.*;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.paginate.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 数据中心控制器
 *
 * @Author: Hor
 * @Date: 2024/12/20 23:03
 * @Version: 1.0
 */
@RestController
@ResponseResult
@RequiredArgsConstructor
@RequestMapping("/api/console/dataCenter")
public class DataCenterController {


    @Resource
    private UserApiRelationServiceImpl userApiRelationService;

    @Resource
    private UserPackageServiceImpl userPackageService;

    @Resource
    private ApiLogServiceImpl apiLogService;

    @Resource
    private ApiTokenService apiTokenService;

    @Resource
    private PackageServiceImpl packageService;

    /**
     * 分页获取申请的接口
     */
    @SaCheckLogin
    @GetMapping("apiList")
    public Page<ApiRelationVo> getApiList(@Valid ApiInfoQueryRequest request) {
        // 获取当前用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        Page<ApiRelationVo> apiRelationVoPage = userApiRelationService.selectUserApiRelationByPage(request, userId);
        apiRelationVoPage.getRecords().forEach(data -> data.setQuota(userPackageService.getQuota(data.getUserId(),data.getApiId())));
        return apiRelationVoPage;
    }

    /**
     * 获取当接口统计
     */
    @SaCheckLogin
    @PostMapping("line")
    public List<ChartDataVo> getMonitorLine(@RequestBody MonitorLineRequest request) {
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

    /**
     * 获取当前接口的该用户的流量包
     */
    @SaCheckLogin
    @GetMapping("getPackage")
    public Page<UserPackage> getUserPackageList(@Valid PageRequest request, Long apiId) {
        // 获取当前用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        return packageService.getUserPackageList(request, userId, apiId);
    }

    /**
     * 获取当前接口分配的Token
     */
    @SaCheckLogin
    @GetMapping("allocationTokens/{apiId}")
    public List<AllocationTokenVO> getAllocationTokens(@PathVariable Long apiId) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        return apiTokenService.getUserAllocationTokens(currentUserId, apiId);
    }


    /**
     * 获取当前接口分配的Token
     */
    @SaCheckLogin
    @GetMapping("allocationToken/{id}")
    public ApiToken getAllocationTokenById(@PathVariable Long id) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        return apiTokenService.getApiTokenByIdAndUserId(id, currentUserId);
    }

    /**
     * 重置当前接口分配的Token
     */
    @SaCheckLogin
    @PutMapping("restAllocationToken/{id}")
    public Boolean restAllocationToken(@PathVariable Long id) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        return apiTokenService.restAllocationTokenUsedQuota(id,currentUserId);
    }

    /**
     * 新增当前接口分配的Token
     */
    @SaCheckLogin
    @PostMapping("allocationToken")
    public Boolean addAllocationToken(@RequestBody AllocationTokenAddRequest addRequest) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        return apiTokenService.save(ApiToken.builder()
                        .tokenId(addRequest.getTokenId())
                        .apiId(addRequest.getApiId())
                        .userId(currentUserId)
                        .totalQuota(addRequest.getTotalQuota())
                .build());
    }

    /**
     * 修改当前接口分配的Token
     */
    @SaCheckLogin
    @PutMapping("allocationToken")
    public Boolean editAllocationToken(@RequestBody AllocationTokenEditRequest editRequest) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        return apiTokenService.updateApiTokenByUserIdAndApiIdAndTokenId(editRequest, currentUserId);
    }

    @DeleteMapping("allocationToken/{id}")
    public Boolean deleteAllocationToken(@PathVariable Long id) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        return apiTokenService.removeApiTokenByIdAndUserId(id,currentUserId);
    }


}
