package cn.apimix.controller.console;

import cn.apimix.api.model.entity.ApiVersion;
import cn.apimix.api.model.req.ApiQueryRequest;
import cn.apimix.api.service.ApiReleaseService;
import cn.apimix.api.service.ApiVersionService;
import cn.apimix.audit.model.entity.table.AuditTableDef;
import cn.apimix.audit.service.impl.AuditServiceImpl;
import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.core.model.PageRequest;
import cn.apimix.audit.model.req.AuditAddRequest;
import cn.apimix.audit.model.entity.AuditRecord;
import cn.apimix.user.model.entity.User;
import cn.apimix.user.model.entity.UserRole;
import cn.apimix.user.model.entity.table.UserRoleTableDef;
import cn.apimix.user.model.mapstruct.UserMapping;
import cn.apimix.user.model.resp.user.UserInfoResp;
import cn.apimix.user.service.impl.UserRoleServiceImpl;
import cn.apimix.user.service.impl.UserServiceImpl;
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

    private final ApiReleaseService apiReleaseService;

    private final ApiVersionService apiVersionService;


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

        boolean auditExists = auditService.exists(new QueryWrapper()
                .where(AuditTableDef.AUDIT.FLOW_NO.eq(addRequest.getFlowNo()))
                .and(AuditTableDef.AUDIT.STATUS.eq(1))
        );

        if (exists) {

            if (auditExists) {
                return auditService.updateAudit(
                        addRequest.getFlowNo(),
                        userId,
                        addRequest.getStatus(),
                        addRequest.getRemark()
                );
            }

            return false;
        }

        // 分配角色
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
    public Page<ApiVersion> getAuditApiByPage(@Valid ApiQueryRequest queryRequest) {
        return apiVersionService.getApiVersionByAudit(queryRequest,1);
    }


    /**
     * 修改审核
     */
    @SaCheckLogin
    @SaCheckPermission("sys:api:audit")
    @PostMapping("passApi")
    public Boolean addAudit(@RequestBody @Valid AuditAddRequest addRequest) {
        Long userId = StpUtil.getLoginIdAsLong();
        ApiVersion apiVersion = apiVersionService.getById(addRequest.getFlowNo());
        // 如果是通过审核将接口修改为上线
        auditService.updateAudit(
                addRequest.getFlowNo(),
                userId,
                addRequest.getStatus(),
                addRequest.getRemark()
        );
        return apiReleaseService.updateStatusByOnlineOrOffLine(apiVersion.getApiId(), userId,addRequest.getStatus() == 2);
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
