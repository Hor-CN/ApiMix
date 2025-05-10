package cn.apimix.service.impl;

import cn.apimix.core.model.PageRequest;
import cn.apimix.core.pay.AliPay;
import cn.apimix.core.pay.AlipayTradeStatusEnum;
import cn.apimix.mapper.ApiInfoMapper;
//import cn.apimix.mapper.PackageMapper;
import cn.apimix.mapper.ProductOrderMapper;
import cn.apimix.model.entity.ApiInfo;
import cn.apimix.model.entity.Package;
import cn.apimix.model.entity.ProductOrder;
import cn.apimix.model.entity.table.ProductOrderTableDef;
import cn.apimix.core.pay.PayStatusEnum;
import cn.apimix.model.enums.PayTypeEnum;
import cn.apimix.model.vo.ProductOrderVo;
import cn.apimix.service.ProductOrderService;
import cn.apimix.user.service.UserAccountService;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Assert;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static cn.apimix.core.pay.PayStatusEnum.*;

/**
 * @Author: Hor
 * @Date: 2024/6/24 下午2:19
 * @Version: 1.0
 */
@Slf4j
@Service
public class ProductOrderServiceImpl extends ServiceImpl<ProductOrderMapper, ProductOrder> implements ProductOrderService {

//    @Resource
//    private PackageMapper packageMapper;

    @Resource
    private ApiInfoMapper apiInfoMapper;

    @Resource
    private UserAccountService userAccountService;

    @Resource
    private AliPay aliPay;

    public ProductOrderVo saveOrder(Long userId, Long price) {
        // 5分钟有效期
        LocalDateTime expirationTime = LocalDateTimeUtil.offset(LocalDateTimeUtil.now(), 5, ChronoUnit.MINUTES);

        ProductOrder order = ProductOrder.builder()
                .type(1)
                .status(NOTPAY)
                .payType(PayTypeEnum.Alipay)
                .price(price)
                .expirationTime(expirationTime)
                .userId(userId)
                .build();
        boolean saveResult = save(order);

        BigDecimal amount = new BigDecimal(price);

        BigDecimal scaledAmount = amount.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        String codeUrl = aliPay.getQrCode(order.getId() + "", "ApiMix " + price + " 积分", String.valueOf(scaledAmount), "捐赠获取积分", "6m", null);

        order.setFormData(codeUrl);
        boolean updateResult  = updateById(order);

        Assert.isTrue(saveResult & updateResult,"操作失败");

        return ProductOrderVo.builder()
                .orderNo(order.getId())
                .payType(order.getPayType())
                .price(order.getPrice())
                .userId(order.getUserId())
                .codeUrl(codeUrl)
                .expirationTime(order.getExpirationTime())
                .type(order.getType())
                .status(order.getStatus())
                .build();
    }

    public ProductOrderVo getOrder(Long userId, Long price) {

        ProductOrder one = getOne(query()
                .where(ProductOrderTableDef.PRODUCT_ORDER.USER_ID.eq(userId))
                .and(ProductOrderTableDef.PRODUCT_ORDER.TYPE.eq(1))
                .and(ProductOrderTableDef.PRODUCT_ORDER.PRICE.eq(price))
                .and(ProductOrderTableDef.PRODUCT_ORDER.PAY_TYPE.eq(PayTypeEnum.Alipay))
                .and(ProductOrderTableDef.PRODUCT_ORDER.STATUS.eq(NOTPAY))
        );
        if (one == null) {
            return null;
        }

        return ProductOrderVo.builder()
                .orderNo(one.getId())
                .type(one.getType())
                .userId(one.getUserId())
                .codeUrl(one.getFormData())
                .price(one.getPrice())
                .payType(one.getPayType())
                .status(one.getStatus())
                .invoice(one.getInvoice())
                .createTime(one.getCreateTime())
                .expirationTime(one.getExpirationTime())
                .build();
    }




    public Page<ProductOrderVo> selectProductOrderByUserId(PageRequest pageRequest, Long userId, Integer type) {
//        Page<ProductOrder> paginate = getMapper().paginate(pageRequest.getPageNumber(), pageRequest.getPageSize(),
//                query().where(ProductOrderTableDef.PRODUCT_ORDER.USER_ID.eq(userId))
//                        .and(ProductOrderTableDef.PRODUCT_ORDER.TYPE.eq(type))
//        );
//
//        Page<ProductOrderVo> pageVo = new Page<>();
//        pageVo.setPageNumber(paginate.getPageNumber());
//        pageVo.setPageSize(paginate.getPageSize());
//        pageVo.setTotalPage(paginate.getTotalPage());
//        pageVo.setTotalRow(paginate.getTotalRow());
//
//        List<ProductOrderVo> productOrderVos = new ArrayList<>();
//        paginate.getRecords().forEach(item -> {
//            ProductOrderVo productOrderVo = ProductOrderVo.builder()
//                    .orderNo(item.getId())
//                    .type(item.getType())
//                    .userId(item.getUserId())
//                    .price(item.getPrice())
//                    .payType(item.getPayType())
//                    .status(item.getStatus())
//                    .count(item.getCount())
//                    .invoice(item.getInvoice())
//                    .createTime(item.getCreateTime())
//                    .expirationTime(item.getExpirationTime())
//                    .build();
//            if (item.getPackageId() != null) {
//                Package aPackage = packageMapper.selectOneById(item.getPackageId());
//                productOrderVo.setPackageInfo(aPackage);
//                ApiInfo apiInfo = apiInfoMapper.selectOneById(aPackage.getApiId());
//                productOrderVo.setProductName(apiInfo.getName());
//            }
//            productOrderVos.add(productOrderVo);
//
//
//        });
//        pageVo.setRecords(productOrderVos);
        return null;
    }


    @Transactional(rollbackFor = Exception.class)
    public void processingTimedOutOrders(ProductOrder productOrder) {
        // 订单ID
        Long orderNo = productOrder.getId();

        String tradeQuery = aliPay.tradeQuery(Long.toString(orderNo));
        // 本地创建了订单,但是用户没有扫码,支付宝端没有订单
        if (tradeQuery == null) {
            this.updateOrderStatusByOrderNo(orderNo, CLOSED);
            log.info("超时订单{},更新成功", orderNo);
            return;
        }

        String tradeStatus = AlipayTradeStatusEnum.findByName(tradeQuery).getPayStatusEnum().getStatus();
        // 订单没有支付就关闭订单,更新本地订单状态
        if (tradeStatus.equals(NOTPAY.getStatus()) || tradeStatus.equals(CLOSED.getStatus())) {
            aliPay.closeTrade(Long.toString(orderNo));
            this.updateOrderStatusByOrderNo(orderNo,CLOSED);
            log.info("超时订单{},关闭成功", orderNo);
            return;
        }

        if (tradeStatus.equals(SUCCESS.getStatus())) {
            // 订单已支付更新商户端的订单状态
            boolean updateOrderStatus = this.updateOrderStatusByOrderNo(orderNo, SUCCESS);
            // 补发积分到用户钱包
            boolean addWalletBalance = userAccountService.increaseAmount(productOrder.getUserId(), productOrder.getPrice());
            Assert.isTrue(updateOrderStatus & addWalletBalance,"操作失败");
            log.info("超时订单{},更新成功", orderNo);
        }

    }

    public boolean updateOrderStatusByOrderNo(Long outTradeNo, PayStatusEnum orderStatus) {
        ProductOrder productOrder = new ProductOrder();
        productOrder.setStatus(orderStatus);
        return update(productOrder,query().where(ProductOrderTableDef.PRODUCT_ORDER.ID.eq(outTradeNo)));
    }



}
