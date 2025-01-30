package cn.apimix.controller.console;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.core.model.PageRequest;
import cn.apimix.model.vo.ProductOrderVo;
import cn.apimix.service.impl.ProductOrderServiceImpl;
import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.paginate.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author: Hor
 * @Date: 2024/12/25 21:15
 * @Version: 1.0
 */

@RestController
@RequiredArgsConstructor
@ResponseResult
@RequestMapping("/api/console/store")
public class StoreController {

    private final ProductOrderServiceImpl productOrderService;

    @GetMapping("/order")
    public Page<ProductOrderVo> getOrder(@Valid PageRequest request, Integer type) {
        // 获取当前用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        return productOrderService.selectProductOrderByUserId(request, userId, type);
    }

}
