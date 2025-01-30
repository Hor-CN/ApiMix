package cn.apimix.controller.console;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.core.model.PageRequest;
import cn.apimix.model.dto.api.ApiInfoQueryRequest;
import cn.apimix.model.dto.api.AuditAddRequest;
import cn.apimix.model.entity.ApiInfo;
import cn.apimix.model.entity.AuditRecord;
import cn.apimix.model.entity.User;
import cn.apimix.model.entity.UserRole;
import cn.apimix.model.entity.table.UserRoleTableDef;
import cn.apimix.model.mapstruct.UserMapping;
import cn.apimix.model.vo.user.UserInfoResp;
import cn.apimix.service.impl.ApiServiceImpl;
import cn.apimix.service.impl.AuditServiceImpl;
import cn.apimix.service.impl.UserRoleServiceImpl;
import cn.apimix.service.impl.UserServiceImpl;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/12/25 22:37
 * @Version: 1.0
 */
@RestController
@ResponseResult
@RequiredArgsConstructor
@RequestMapping("/api/console/audit")
public class AuditController {


    private final AuditServiceImpl auditService;

    private final UserRoleServiceImpl userRoleService;

    private final UserServiceImpl userService;

    private final UserMapping userMapping;

    private final ApiServiceImpl apiService;




    /**
     * 获取待审核的开发者认证申请
     */
    @SaCheckLogin
    @SaCheckPermission("sys:apply:dev")
    @GetMapping("devs")
    public Page<UserInfoResp> getAuditDevByPage(@Valid PageRequest request) {
        Page<User> userPage = userService.selectAuditUserDevByPage(request);
        List<UserInfoResp> userInfoResps = userMapping.usersToUserVos(userPage.getRecords());
        return new Page<>(userInfoResps, userPage.getPageNumber(), userPage.getPageSize(), userPage.getTotalPage());
    }

    /**
     * 通过开发者认证审核
     */
    @SaCheckLogin
    @SaCheckPermission("sys:apply:dev")
    @PostMapping("passDev")
    public Boolean updateAudit(@RequestBody @Valid AuditAddRequest addRequest) {
        Long userId = StpUtil.getLoginIdAsLong();
        // 如果是通过审核将用户添加开发者权限
        // 如果本身是开发者就不添加
        boolean exists = userRoleService.exists(
                new QueryWrapper().where(UserRoleTableDef.USER_ROLE.USER_ID.eq(addRequest.getFlowNo()))
                        .and(UserRoleTableDef.USER_ROLE.ROLE_ID.eq(3))
        );
        if (exists) {
            return false;
        }
        userRoleService.save(UserRole.builder()
                .userId(addRequest.getFlowNo())
                .roleId(3)
                .build());

        return auditService.updateAudit(
                addRequest.getFlowNo(),
                userId,
                addRequest.getStatus(),
                addRequest.getRemark()
        );
    }


    /**
     * 获取待审核的接口
     */
    @SaCheckLogin
    @SaCheckPermission("sys:api:list")
    @GetMapping("apis")
    public Page<ApiInfo> getAuditApiByPage(@Valid ApiInfoQueryRequest queryRequest) {
        return apiService.selectAuditApiInfoByPage(queryRequest);
    }

    /**
     * 修改审核
     */
    @SaCheckLogin
    @SaCheckPermission("sys:api:audit")
    @PostMapping("passApi")
    public Boolean addAudit(@RequestBody @Valid AuditAddRequest addRequest) {
        Long userId = StpUtil.getLoginIdAsLong();
        // 如果是通过审核将接口修改为上线
        apiService.onLineOrOffLine(addRequest.getFlowNo(), addRequest.getStatus() == 2);
        return auditService.updateAudit(
                addRequest.getFlowNo(),
                userId,
                addRequest.getStatus(),
                addRequest.getRemark()
        );
    }


    /**
     * 获取接口审核明细
     */
    @SaCheckLogin
    @SaCheckPermission("dev:api:audit")
    @GetMapping("{apiId}")
    public List<AuditRecord> getAuditRecords(@PathVariable Long apiId) {
        return auditService.selectAuditRecordByFlowNo(apiId);
    }


}
