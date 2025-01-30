package cn.apimix.controller.system;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.core.model.IdRequest;
import cn.apimix.model.dto.api.ApiAddRequest;
import cn.apimix.service.impl.ApiServiceImpl;
import cn.apimix.service.impl.AuditServiceImpl;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author: Hor
 * @Date: 2024/5/29 19:45
 * @Version: 1.0
 */
@RestController
@ResponseResult
@RequestMapping("/api/system/interface")
public class SysApiController {

    @Resource
    private ApiServiceImpl apiService;

    @Resource
    private AuditServiceImpl auditService;

    /**
     * 添加接口。
     *
     * @param addRequest dto
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @SaCheckLogin
    @SaCheckPermission("sys:api:add")
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
    @SaCheckPermission("sys:api:del")
    @PostMapping("delApi")
    public boolean deleteApi(@RequestBody @Valid IdRequest idRequest) {
        return apiService.deleteApiInfoByApiId(idRequest.getId());
    }




}
