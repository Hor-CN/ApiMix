package cn.apimix.service.impl;

import cn.apimix.common.enums.HttpStatusEnum;
import cn.apimix.core.config.AliPayAccountConfig;
import cn.apimix.core.exception.HorApiException;
import cn.apimix.core.pay.AliPay;
import cn.apimix.core.pay.AliPayAsyncResponse;
import cn.apimix.core.pay.AlipayTradeStatusEnum;
import cn.apimix.core.pay.PayStatusEnum;
import cn.apimix.core.utils.RedisUtils;
import cn.apimix.core.utils.RedissonLockUtil;
import cn.apimix.model.entity.ProductOrder;
import cn.apimix.model.entity.table.ProductOrderTableDef;
import cn.apimix.model.enums.PayTypeEnum;
import cn.apimix.model.vo.PayOrderStatusVo;
import cn.apimix.model.vo.ProductOrderVo;
import cn.apimix.service.OrderService;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.ijpay.alipay.AliPayApi;
import com.ijpay.alipay.AliPayApiConfigKit;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static cn.apimix.core.pay.PayStatusEnum.SUCCESS;

/**
 * @Author: Hor
 * @Date: 2025/1/23 09:52
 * @Version: 1.0
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private RedissonLockUtil redissonLockUtil;

    @Resource
    private ProductOrderServiceImpl productOrderService;

    @Resource
    private PackageServiceImpl packageService;

    @Resource
    private AliPayAccountConfig aliPayAccountConfig;

    @Resource
    private UserAccountServiceImpl userAccountService;

    @Resource
    private AliPay aliPay;

    @Resource
    private UserServiceImpl userService;
    /**
     * 处理订单通知
     *
     * @param request 要求
     * @return {@link String}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String doOrderNotify(HttpServletRequest request) {
        Map<String, String> params = AliPayApi.toMap(request);
        AliPayAsyncResponse aliPayAsyncResponse = JSONUtil.toBean(JSONUtil.toJsonStr(params), AliPayAsyncResponse.class);

        String lockName = "AliPayOrderNotify:lock:" + aliPayAsyncResponse.getOutTradeNo();
        return redissonLockUtil.redissonDistributedLocks(lockName, "【支付宝异步回调异常】:", () -> {
            String result;
            try {
                result = checkAlipayOrder(aliPayAsyncResponse, params);
            } catch (AlipayApiException e) {
                throw new HorApiException(HttpStatusEnum.FAIL);
            }
            if (!"success".equals(result)) {
                return result;
            }
            String doAliPayOrderBusinessResult = this.doAliPayOrderBusiness(aliPayAsyncResponse);
            if (StringUtils.isBlank(doAliPayOrderBusinessResult)) {
                throw new HorApiException(HttpStatusEnum.FAIL);
            }
            return doAliPayOrderBusinessResult;
        });
    }

    private String checkAlipayOrder(AliPayAsyncResponse response, Map<String, String> params) throws AlipayApiException {
        String result = "failure";
        boolean verifyResult = AlipaySignature.rsaCheckV1(params, AliPayApiConfigKit.getAliPayApiConfig().getAliPayPublicKey(),
                AliPayApiConfigKit.getAliPayApiConfig().getCharset(),
                AliPayApiConfigKit.getAliPayApiConfig().getSignType());
        if (!verifyResult) {
            return result;
        }
        // 1.验证该通知数据中的 out_trade_no 是否为商家系统中创建的订单号。
        ProductOrder productOrder = productOrderService.getById(response.getOutTradeNo());
        if (productOrder == null) {
            log.error("订单不存在");
            return result;
        }
        // 2.判断 total_amount 是否确实为该订单的实际金额（即商家订单创建时的金额）。
        int totalAmount = new BigDecimal(response.getTotalAmount()).multiply(new BigDecimal("100")).intValue();
        if (totalAmount != productOrder.getPrice()) {
            log.error("订单金额不一致");
            return result;
        }
        // 3.校验通知中的 seller_id（或者 seller_email) 是否为 out_trade_no 这笔单据的对应的操作方（有的时候，一个商家可能有多个 seller_id/seller_email）。
        String sellerId = aliPayAccountConfig.getSellerId();
        if (!response.getSellerId().equals(sellerId)) {
            log.error("卖家账号校验失败");
            return result;
        }
        // 4.验证 app_id 是否为该商家本身。
        String appId = aliPayAccountConfig.getAppId();
        if (!response.getAppId().equals(appId)) {
            log.error("校验失败");
            return result;
        }
        // 状态 TRADE_SUCCESS 的通知触发条件是商家开通的产品支持退款功能的前提下，买家付款成功。
        String tradeStatus = response.getTradeStatus();
        if (!"TRADE_SUCCESS".equals(tradeStatus)) {
            log.error("交易失败");
            return result;
        }
        return "success";
    }

    @SneakyThrows
    public String doAliPayOrderBusiness(AliPayAsyncResponse response) {
        String outTradeNo = response.getOutTradeNo();
        ProductOrder productOrder = productOrderService.getById(outTradeNo);
        // 处理重复通知
        if (SUCCESS.equals(productOrder.getStatus())) {
            return "success";
        }
        // 业务代码
        // 更新订单状态
        boolean updateOrderStatus = productOrderService.updateOrderStatusByOrderNo(Long.valueOf(outTradeNo), SUCCESS);
        // 更新用户积分
        boolean addWalletBalance = userAccountService.increaseAmount(productOrder.getUserId(), productOrder.getPrice());
        if ( updateOrderStatus && addWalletBalance) {
            // 获取用户邮箱
            String email = userService.selectUserById(productOrder.getUserId()).getEmail();
            // 有邮箱时才发送
            if (!email.isEmpty()) {
                aliPay.sendPaySuccessEmail(productOrder.getUserId(), email, response.getTotalAmount(),String.valueOf(new BigDecimal(response.getTotalAmount()).multiply(new BigDecimal("100"))));
            }
            RedisUtils.delete("query:orderStatus:" + productOrder.getId());

            log.info("【支付回调通知处理成功】");

            return "success";
        }
        throw new HorApiException(HttpStatusEnum.FAIL);
    }


    /**
     * 创建订单
     *
     * @param userId    用户ID
     * @param packageId 套餐ID
     * @param count     购买数量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createProductOrder(Long userId, Long packageId, Integer count) {
        String redissonLock = ("createOrder:" + userId).intern();
        // 分布式锁工具
        redissonLockUtil.redissonDistributedLocks(redissonLock, () -> {
            // 保存订单
            packageService.purchasePackage(packageId, userId, count);
        });
    }

    @Override
    public ProductOrderVo createPayOrder(Long userId, Long price) {
        String redissonLock = ("getOrder:" + userId).intern();

        ProductOrderVo getOrderVo = redissonLockUtil.redissonDistributedLocks(redissonLock, () -> {
            // 订单存在就返回不再新创建
            return productOrderService.getOrder(userId, price);
        });

        if (getOrderVo != null) {
            return getOrderVo;
        }
        redissonLock = ("createOrder:" + userId).intern();
        // 分布式锁工具
        return redissonLockUtil.redissonDistributedLocks(redissonLock, () -> {
            // 保存订单,返回vo信息
            return productOrderService.saveOrder(userId, price);
        });
    }

    /**
     * 按时间获得未支付订单
     *
     * @param minutes 分钟
     * @param remove  是否是删除
     * @param payType 付款类型
     * @return {@link List}<{@link ProductOrder}>
     */
    @Override
    public List<ProductOrder> getNoPayOrderByDuration(Integer minutes, Boolean remove, PayTypeEnum payType) {

        QueryWrapper query = new QueryWrapper();
        query.where(ProductOrderTableDef.PRODUCT_ORDER.PAY_TYPE.eq(payType));
        query.where(ProductOrderTableDef.PRODUCT_ORDER.STATUS.eq(PayStatusEnum.NOTPAY));
        // 删除
        if (remove) {
            query.or(ProductOrderTableDef.PRODUCT_ORDER.STATUS.eq(PayStatusEnum.CLOSED));
        }
        if (minutes == null) {
            query.and(ProductOrderTableDef.PRODUCT_ORDER.EXPIRATION_TIME.le(LocalDateTimeUtil.now()));
        }else {
            query.and(ProductOrderTableDef.PRODUCT_ORDER.CREATE_TIME.le(LocalDateTimeUtil.now().minusMinutes(minutes)));
        }
        return productOrderService.list(query);
    }


    public PayOrderStatusVo getOrderStatus(String orderNo) {
        ProductOrder productOrder = productOrderService.getById(orderNo);

        Assert.notNull(productOrder,"订单不存在");

        PayStatusEnum payStatusEnum = productOrder.getStatus();
        return PayOrderStatusVo.builder()
                .msg(payStatusEnum.getText())
                .status(payStatusEnum.getStatus())
                .state(AlipayTradeStatusEnum.getState(payStatusEnum))
                .expirationTime(productOrder.getExpirationTime())
                .build();
    }


}
