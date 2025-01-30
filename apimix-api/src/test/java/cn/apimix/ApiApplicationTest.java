package cn.apimix;

import cn.apimix.core.config.AliPayAccountConfig;
import cn.apimix.model.enums.PayTypeEnum;
import cn.apimix.service.impl.OrderServiceImpl;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.GoodsDetail;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.ijpay.alipay.AliPayApi;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @Author: Hor
 * @Date: 2025/1/20 19:02
 * @Version: 1.0
 */

@Slf4j
@SpringBootTest
public class ApiApplicationTest {

    @Resource
    AliPayAccountConfig aliPayAccountConfig;

    @Resource
    OrderServiceImpl orderService;

    String OutTradeNo = "order_97460568736566706666";

    @Test
    void alipayQuery() {
        System.out.println(orderService.getNoPayOrderByDuration(null, false, PayTypeEnum.Alipay));
    }

    @Test
    @SneakyThrows
    void alipayCreateOrder() {
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setOutTradeNo(OutTradeNo);
        model.setSubject("ApiMix测试");
        model.setTotalAmount("0.01");
        model.setBody("测试商品");

        AlipayTradePrecreateRequest alipay_request = new AlipayTradePrecreateRequest ();
        alipay_request.setBizModel(model);
        alipay_request.setNotifyUrl(aliPayAccountConfig.getNotifyUrl());
        alipay_request.setReturnUrl(aliPayAccountConfig.getReturnUrl());

        AlipayTradePrecreateResponse alipayTradePagePayResponse = AliPayApi.execute(alipay_request);

        System.err.println(alipayTradePagePayResponse.getQrCode());
    }

    @Test
    @SneakyThrows
    void queryOrder() {


        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo("123");
        request.setBizModel(model);

        AlipayTradeQueryResponse response = AliPayApi.execute(request);
        System.out.println(response);

        System.out.println(response.getTradeStatus());
        System.out.println(response.getBody());

    }


}