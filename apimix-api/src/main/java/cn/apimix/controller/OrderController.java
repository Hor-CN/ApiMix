package cn.apimix.controller;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.core.utils.RedisUtils;
import cn.apimix.model.dto.PayCreateRequest;
import cn.apimix.model.dto.PayTradeQueryRequest;
import cn.apimix.model.vo.PayOrderStatusVo;
import cn.apimix.model.vo.ProductOrderVo;
import cn.apimix.service.impl.OrderServiceImpl;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;

/**
 * @Author: Hor
 * @Date: 2024/8/29 下午5:37
 * @Version: 1.0
 */
@Slf4j
@RestController
@ResponseResult
@RequestMapping("/api/order")
public class OrderController {

    @Resource
    private OrderServiceImpl orderService;

    @SaCheckLogin
    @PostMapping("create")
    public ProductOrderVo createOrder(@RequestBody PayCreateRequest createRequest) {
        // 获取当前用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        return orderService.createPayOrder(userId, createRequest.getPrice());
    }



    /**
     * 解析订单通知结果
     *  在进行异步通知交互时，如果支付宝收到的应答不是 success
     *  支付宝会认为通知失败，会通过一定的策略定期重新发起通知。
     *  重试逻辑为：当未收到success 时立即尝试重发 3 次通知，若 3 次仍不成功，
     *  则后续通知的间隔频率为：4m、10m、10m、1h、2h、6h、15h。
     *  fail	消息获取失败	重试
     *  success	消息获取成功	不重试
     * @param request    请求
     * @return {@link String}
     */

    @PostMapping("/payNotify")
    public String parseOrderNotifyResult(HttpServletRequest request) {
        log.info("支付宝回调>>>:{}", request);
        return orderService.doOrderNotify(request);
    }


    @SaCheckLogin
    @PostMapping("/payOrderQuery")
    public PayOrderStatusVo payOrderSync(@RequestBody PayTradeQueryRequest request) {
        String orderNo = request.getOrderNo();
        PayOrderStatusVo data = RedisUtils.get("query:orderStatus:" + orderNo);
        if (data != null) {
            return data;
        }
        PayOrderStatusVo orderStatus = orderService.getOrderStatus(request.getOrderNo());
        Duration between = LocalDateTimeUtil.between(LocalDateTimeUtil.now(), orderStatus.getExpirationTime());
        if (between.getSeconds() <= 0) {
            RedisUtils.set("query:orderStatus:" + orderNo, orderStatus, Duration.ofMinutes(5));
        }else {
            RedisUtils.set("query:orderStatus:" + orderNo, orderStatus, between);

        }
        return orderStatus;
    }


}
