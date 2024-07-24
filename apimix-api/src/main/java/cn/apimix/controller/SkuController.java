package cn.apimix.controller;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.model.dto.api.PurchaseApiRequest;
import cn.apimix.model.entity.PackageType;
import cn.apimix.service.impl.PackageServiceImpl;
import cn.apimix.service.impl.PackageTypeServiceImpl;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Hor
 * @Date: 2024/6/17 下午5:59
 * @Version: 1.0
 */
@RestController
@ResponseResult
@RequestMapping("/api/sku")
public class SkuController {


    @Resource
    private PackageServiceImpl packageService;


    @Resource
    private PackageTypeServiceImpl packageTypeService;

    /**
     * 根据接口ID获取SKU列表
     */
    @GetMapping("list/{id}")
    public Object getSkuList(@PathVariable Long id) {
        return packageService.getSkuList(id);
    }


    /**
     * 获取套餐可选列表
     */
    @GetMapping("type")
    public List<PackageType> packageTypes() {
        return packageTypeService.getPackageTypeList();
    }


    /**
     * 购买套餐
     */
    @SaCheckLogin
    @PostMapping("purchase")
    public Boolean purchase(@RequestBody PurchaseApiRequest purchaseApiRequest) {
        // 获取当前用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        return packageService.purchasePackage(purchaseApiRequest.getPackageId(), userId, purchaseApiRequest.getCount());
    }


}
