package cn.apimix.job;

import cn.apimix.comment.service.CommentLikeService;
import cn.apimix.comment.service.CommentService;
import cn.apimix.core.utils.RedissonLockUtil;
import cn.apimix.model.entity.ProductOrder;
import cn.apimix.model.enums.PayTypeEnum;
import cn.apimix.service.OrderService;
import cn.apimix.service.impl.ProductOrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Hor
 * @Date: 2025/1/23 19:28
 * @Version: 1.0
 */
@Slf4j
@Component
public class PayJob {

    @Resource
    private OrderService orderService;

    @Resource
    private ProductOrderServiceImpl productOrderService;

    @Resource
    private CommentLikeService commentLikeService;

    @Resource
    private RedissonLockUtil redissonLockUtil;


    /**
     * 支付宝订单确认
     * 每分钟查询一次超过5分钟过期的订单,并且未支付
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void aliPayOrderConfirm() {
        redissonLockUtil.redissonDistributedLocks("aliPayOrderConfirm", () -> {
            List<ProductOrder> orderList = orderService.getNoPayOrderByDuration(null, false, PayTypeEnum.Alipay);
            for (ProductOrder productOrder : orderList) {
                String orderNo = Long.toString(productOrder.getId());
                try {
                    productOrderService.processingTimedOutOrders(productOrder);
                } catch (Exception e) {
                    log.error("支付宝超时订单,{},确认异常：{}", orderNo, e.getMessage());
                    break;
                }
            }
        });
    }

    /**
     * 订单确认
     * 每2点删除一次15天前的订单,并且未支付，并且已关闭的订单
     */
    @Scheduled(cron = "* * 2 * * ?")
    public void clearOverdueOrders() {
        redissonLockUtil.redissonDistributedLocks("clearOverdueOrders", () -> {
            List<ProductOrder> orderList = orderService.getNoPayOrderByDuration(15 * 24 * 60, true, PayTypeEnum.Alipay);
            List<Long> collectId = orderList.stream().map(ProductOrder::getId).collect(Collectors.toList());
            boolean removeResult = productOrderService.removeByIds(collectId);
            if (removeResult) {
                log.info("已关闭的订单清除成功");
            }
        });
    }

    /**
     * 评论数据持久化 每天凌晨3点赞数据持久化同步数据库
     */
    @Scheduled(cron = "* * 3 * * ?")
    public void saveCommentData() {
        redissonLockUtil.redissonDistributedLocks("saveCommentData", () -> {
            if (commentLikeService.syncLike()) {
                log.info("评论数据同步成功");
            }
        });
    }

}
