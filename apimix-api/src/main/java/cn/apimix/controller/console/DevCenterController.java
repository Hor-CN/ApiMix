package cn.apimix.controller.console;

import cn.apimix.api.model.entity.ApiVersion;
import cn.apimix.api.model.req.ApiAddRequest;
import cn.apimix.api.model.req.ApiEditRequest;
import cn.apimix.api.model.req.ApiQueryRequest;
import cn.apimix.api.model.resp.ApiReleaseResp;
import cn.apimix.api.service.ApiReleaseService;
import cn.apimix.api.service.ApiVersionService;
import cn.apimix.audit.service.impl.AuditServiceImpl;
import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.core.model.IdRequest;
import cn.apimix.audit.model.entity.AuditRecord;
import cn.apimix.core.model.PageRequest;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import com.mybatisflex.core.paginate.Page;
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

    private final AuditServiceImpl auditService;

    private final ApiVersionService apiVersionService;

    private final ApiReleaseService apiReleaseService;

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
        return apiReleaseService.publishInterFace(addRequest, userId);
    }

    /**
     * 根据接口ID获取详细信息。
     *
     * @param apiId 接口ID
     * @return 详情
     */
    @SaCheckPermission("dev:api:query")
    @GetMapping("/{apiId}")
    public ApiReleaseResp getApiInfo(@PathVariable Long apiId) {
        return apiReleaseService.getInterfaceById(apiId);
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
        Long userId = apiReleaseService.getById(idRequest.getId()).getUserId();
        // 判断此操作是不是接口作者
        Assert.isTrue(Objects.equals(userId, currentUserId), "无权限");
        return apiReleaseService.deleteInterfaceById(idRequest.getId());
    }


    /**
     * 获取开发者上传的接口列表
     *
     * @param page 分页
     * @return 结果
     */
    @SaCheckLogin
    @GetMapping("list")
    public Page<ApiReleaseResp> getDevInterfaceList(@Valid ApiQueryRequest page) {
        // 获取当前用户ID
        Long loginId = StpUtil.getLoginIdAsLong();
        return apiReleaseService.getDevInterfaceByPage(page, loginId);
    }


    /**
     * 根据主键更新
     */
    @SaCheckLogin
    @SaCheckPermission("dev:api:edit")
    @PostMapping("editApi")
    public Boolean updateInterface(@RequestBody @Valid ApiEditRequest editRequest) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        // 获取要修改接口的作者
        Long userId = apiReleaseService.getById(editRequest.getId()).getUserId();
        // 判断此操作是不是接口作者
        Assert.isTrue(Objects.equals(userId, currentUserId), "无权限");
        return apiReleaseService.updateInterface(editRequest,userId);
    }


    /**
     * 上线接口
     */
    @SaCheckLogin
    @SaCheckPermission("dev:api:edit")
    @PostMapping("online")
    public boolean updateStatusByOnline(@RequestBody @Valid IdRequest idRequest) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        return apiReleaseService.updateStatusByOnlineOrOffLine(idRequest.getId(), currentUserId, true);
    }

    /**
     * 下线接口
     */
    @SaCheckLogin
    @SaCheckPermission("dev:api:edit")
    @PostMapping("offline")
    public boolean updateStatusByOffLine(@RequestBody @Valid IdRequest idRequest) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        return apiReleaseService.updateStatusByOnlineOrOffLine(idRequest.getId(), currentUserId, false);
    }

    /**
     * 获取发布版本
     */
    @SaCheckLogin
    @SaCheckPermission("dev:api:query")
    @GetMapping("releases")
    public Page<ApiVersion> getReleases(@Valid PageRequest page,Long apiId,String name, Integer status) {
        Long loginId = StpUtil.getLoginIdAsLong();
        return apiVersionService.getApiVersionAuditByUserId(page,apiId ,name, status,loginId);
    }

    @SaCheckLogin
    @SaCheckPermission("dev:api:edit")
    @PostMapping("audit")
    public Boolean addAudit(@RequestBody @Valid IdRequest idRequest) {
        return apiVersionService.addApiVersionAudit(idRequest.getId());
    }

}
