package cn.apimix.controller.console;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.core.model.IdRequest;
import cn.apimix.model.dto.api.ApiAddRequest;
import cn.apimix.model.dto.api.ApiEditRequest;
import cn.apimix.model.entity.AuditRecord;
import cn.apimix.model.vo.api.ApiInfoVo;
import cn.apimix.service.impl.ApiServiceImpl;
import cn.apimix.service.impl.AuditServiceImpl;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * 开发者中心
 *
 * @Author: Hor
 * @Date: 2024/12/4 15:29
 * @Version: 1.0
 */
@RestController
@RequiredArgsConstructor
@ResponseResult
@RequestMapping("/api/console/devCenter")
public class DevCenterController {

    private final ApiServiceImpl apiService;

    private final AuditServiceImpl auditService;

    /**
     * 根据接口ID获取详细信息。
     *
     * @param apiId 接口ID
     * @return 详情
     */
    @GetMapping("/{apiId}")
    public ApiInfoVo getApiInfo(@PathVariable Long apiId) {
        return apiService.selectApiInfoByApiId(apiId);
    }

 
    /**
     * 获取接口审核明细
     */
    @SaCheckLogin
    @SaCheckPermission("dev:api:audit")
    @GetMapping("/audit/{apiId}")
    public List<AuditRecord> getAuditRecords(@PathVariable Long apiId) {
        return auditService.selectAuditRecordByFlowNo(apiId);
    }


    /**
     * 添加接口。
     *
     * @param addRequest dto
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @SaCheckLogin
    @SaCheckPermission("dev:api:add")
    @PostMapping("addApi")
    public boolean addApi(@RequestBody @Valid ApiAddRequest addRequest) {
        // 获取当前用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        // 保存接口信息
        return apiService.insertApi(addRequest, userId);
    }

    /**
     * 根据主键删除。
     *
     * @param idRequest 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @SaCheckLogin
    @SaCheckPermission("dev:api:del")
    @PostMapping("delApi")
    public boolean deleteApi(@RequestBody @Valid IdRequest idRequest) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        // 获取要删除接口的作者
        Long userId = apiService.getById(idRequest.getId()).getUserId();
        // 判断此操作是不是接口作者
        Assert.isTrue(Objects.equals(userId, currentUserId), "无权限");
        return apiService.deleteApiInfoByApiId(idRequest.getId());
    }

    /**
     * 根据主键更新
     */
    @SaCheckLogin
    @SaCheckPermission("dev:api:edit")
    @PostMapping("editApi")
    public void updateApi(@RequestBody @Valid ApiEditRequest editRequest) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        // 获取要修改接口的作者
        Long userId = apiService.getById(editRequest.getId()).getUserId();
        // 判断此操作是不是接口作者
        Assert.isTrue(Objects.equals(userId, currentUserId), "无权限");
        apiService.updateApiInfo(editRequest);
    }


    /**
     * 上线接口
     */
    @SaCheckLogin
    @SaCheckPermission("dev:api:online")
    @PostMapping("online")
    public boolean updateStatusByOnline(@RequestBody @Valid IdRequest idRequest) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        return apiService.updateStatusByOnlineOrOffLine(idRequest.getId(), currentUserId, true);
    }

    /**
     * 下线接口
     */
    @SaCheckLogin
    @SaCheckPermission("dev:api:offline")
    @PostMapping("offline")
    public boolean updateStatusByOffLine(@RequestBody @Valid IdRequest idRequest) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        return apiService.updateStatusByOnlineOrOffLine(idRequest.getId(), currentUserId, false);
    }



}
