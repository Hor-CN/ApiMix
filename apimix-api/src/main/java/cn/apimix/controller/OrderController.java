package cn.apimix.controller;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.core.core.model.PageRequest;
import cn.apimix.service.impl.ProductOrderServiceImpl;
import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author: Hor
 * @Date: 2024/8/29 下午5:37
 * @Version: 1.0
 */
@RestController
@ResponseResult
@RequestMapping("/api/order")
public class OrderController {

    @Resource
    private ProductOrderServiceImpl productOrderService;

    @GetMapping("/products")
    public Page<?> getProductOrder(@Valid PageRequest request) {
        // 获取当前用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        return productOrderService.selectProductOrderByUserId(request, userId);
    }
}
