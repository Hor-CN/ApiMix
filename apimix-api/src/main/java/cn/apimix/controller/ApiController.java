package cn.apimix.controller;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.core.core.model.IdRequest;
import cn.apimix.core.core.model.PageRequest;
import cn.apimix.model.dto.api.ApiAddRequest;
import cn.apimix.model.dto.api.ApiEditRequest;
import cn.apimix.model.dto.api.ApiInfoQueryRequest;
import cn.apimix.model.entity.ApiInfo;
import cn.apimix.model.entity.AuditRecord;
import cn.apimix.model.entity.Category;
import cn.apimix.model.entity.UserPackage;
import cn.apimix.model.vo.api.ApiItemVo;
import cn.apimix.model.vo.api.ApiRelationVo;
import cn.apimix.service.impl.*;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @Author: Hor
 * @Date: 2024/5/25 17:51
 * @Version: 1.0
 */
@RestController
@ResponseResult
@RequestMapping("/api/interface")
public class ApiController {

    @Resource
    private ApiServiceImpl apiService;

    @Resource
    private CategoryServiceImpl categoryService;

    @Resource
    private UserApiRelationServiceImpl userApiRelationService;

    @Resource
    private PackageServiceImpl packageService;

    @Resource
    private UserPackageServiceImpl userPackageService;

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
    public boolean updateApi(@RequestBody @Valid ApiEditRequest editRequest) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        // 获取要修改接口的作者
        Long userId = apiService.getById(editRequest.getId()).getUserId();
        // 判断此操作是不是接口作者
        Assert.isTrue(Objects.equals(userId, currentUserId), "无权限");
        //Todo 修改接口
        return true;
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


    /**
     * 获取当前开发者贡献的接口
     */

    @SaCheckLogin
    @GetMapping("count")
    public Long getUserDevApiByCount() {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        return apiService.selectApiBycCount(currentUserId);
    }


    @Resource
    private AuditServiceImpl auditService;

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
     * 根据主键获取详细信息。
     *
     * @param apiId 主键
     * @return 详情
     */
    @GetMapping("/{apiId}")
    public ApiItemVo getApiInfo(@PathVariable Long apiId) {
        return apiService.selectApiInfoByApiId(apiId);
    }

    /**
     * 分页查询所有接口根据分类ID
     *
     * @param categoryId 分类ID
     * @return 分页对象
     */
    @GetMapping("list")
    public Page<ApiInfo> getApiLists(@Valid ApiInfoQueryRequest page, Long categoryId) {
        return apiService.selectApiInfoByCategory(page, categoryId);
    }

    /**
     * 获取开发者上传的接口列表
     *
     * @param page 分页
     * @return 结果
     */
    @SaCheckLogin
    @GetMapping("getDevApiList")
    public Page<ApiInfo> getDevApiList(@Valid ApiInfoQueryRequest page) {
        // 获取当前用户ID
        Long loginId = StpUtil.getLoginIdAsLong();
        return apiService.selectDevApiInfoByPage(page, loginId);
    }


    /**
     * 获取接口分类列表
     */
    @GetMapping("category")
    public List<Category> getCategoryList() {
        return categoryService.selectCategoryByList();
    }

    /**
     * 分页获取申请的接口
     */
    @SaCheckLogin
    @GetMapping("getShop")
    public Page<ApiRelationVo> getShopList(@Valid PageRequest request) {
        // 获取当前用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        Page<ApiRelationVo> apiRelationVoPage = userApiRelationService.selectUserApiRelationByPage(request, userId);
        apiRelationVoPage.getRecords().forEach(data -> data.setQuota(userPackageService.getQuota(data.getUserId(),data.getApiId())));
        return apiRelationVoPage;
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

}
