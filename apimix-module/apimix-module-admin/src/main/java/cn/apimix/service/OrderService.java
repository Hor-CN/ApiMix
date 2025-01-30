package cn.apimix.service;

import cn.apimix.model.entity.ProductOrder;
import cn.apimix.model.enums.PayTypeEnum;
import cn.apimix.model.vo.ProductOrderVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 订单服务
 *
 * @Author: Hor
 * @Date: 2025/1/21 22:12
 * @Version: 1.0
 */
public interface OrderService {

    /**
     * 处理订单通知
     *
     * @param request    要求
     * @return {@link String}
     */
    String doOrderNotify(HttpServletRequest request);


    /**
     * 创建商品订单
     *
     * @param userId 用户ID
     * @param packageId 套餐ID
     * @param count 购买数量
     */
    void createProductOrder(Long userId, Long packageId, Integer count);



    ProductOrderVo createPayOrder(Long userId, Long amount);



    /**
     * 按时间获得未支付订单
     *
     * @param minutes 分钟
     * @param remove  是否是删除
     * @param payType 付款类型
     * @return {@link List}<{@link ProductOrder}>
     */
    List<ProductOrder> getNoPayOrderByDuration(Integer minutes, Boolean remove, PayTypeEnum payType);




}
