package cn.apimix.controller.system;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.model.dto.system.option.OptionQuery;
import cn.apimix.model.dto.system.option.OptionReq;
import cn.apimix.model.dto.system.option.OptionResetValueReq;
import cn.apimix.model.dto.system.option.OptionResp;
import cn.apimix.service.impl.SysOptionServiceImpl;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2025/1/25 22:02
 * @Version: 1.0
 */
@RestController
@ResponseResult
@RequestMapping("/api/system/option")
public class SysOptionController {


    @Resource
    private SysOptionServiceImpl optionService;

    @GetMapping
    public List<OptionResp> list(@Validated OptionQuery query) {
        return optionService.list(query);
    }

    @SaCheckPermission("system:config:update")
    @PutMapping
    public void update(@Valid @RequestBody List<OptionReq> options) {
        optionService.update(options);
    }


    @SaCheckPermission("system:config:reset")
    @PatchMapping("/value")
    public void resetValue(@Validated @RequestBody OptionResetValueReq req) {
        optionService.resetValue(req);
    }

}
