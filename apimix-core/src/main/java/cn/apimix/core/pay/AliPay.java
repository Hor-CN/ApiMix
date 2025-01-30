package cn.apimix.core.pay;

import cn.apimix.core.config.AliPayAccountConfig;
import cn.apimix.core.exception.HorApiException;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.ijpay.alipay.AliPayApi;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * @Author: Hor
 * @Date: 2025/1/20 21:30
 * @Version: 1.0
 */

@Slf4j
@Component
public class AliPay {

    @Resource
    AliPayAccountConfig aliPayAccountConfig;


    /**
     * 获取支付二维码
     *
     * @param outTradeNo           商户订单号
     * @param subject              订单标题
     * @param totalAmount          订单总金额
     * @param body                 商品详细地描述
     * @param qrCodeTimeoutExpress 二维码过期时间 m 分钟 h 小时 d天
     * @param callBackUrl          单独回调地址，传null使用默认通知地址
     */
    @SneakyThrows
    public String getQrCode(String outTradeNo, String subject, String totalAmount, String body, String qrCodeTimeoutExpress, String callBackUrl) {
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setOutTradeNo(outTradeNo);
        model.setSubject(subject);
        model.setTotalAmount(totalAmount);
        model.setBody(body);
        model.setQrCodeTimeoutExpress(qrCodeTimeoutExpress);

        // 构建支付请求参数
        AlipayTradePrecreateRequest alipayRequest = new AlipayTradePrecreateRequest();
        alipayRequest.setBizModel(model);

        alipayRequest.setNotifyUrl(callBackUrl != null ? callBackUrl : aliPayAccountConfig.getNotifyUrl());

//        alipayRequest.setReturnUrl(aliPayAccountConfig.getReturnUrl());

        // 预下单
        AlipayTradePrecreateResponse alipayTradePagePayResponse = AliPayApi.execute(alipayRequest);
        // 获取收款码URL
        return alipayTradePagePayResponse.getQrCode();
    }

    /**
     * 查询订单状态
     *
     * @param outTradeNo 商户订单号
     * @return 订单交易状态：
     * WAIT_BUYER_PAY（交易创建，等待买家付款）、
     * TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、
     * TRADE_SUCCESS（交易支付成功）、
     * TRADE_FINISHED（交易结束，不可退款）
     */
    @SneakyThrows
    public String tradeQuery(String outTradeNo) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(outTradeNo);
        request.setBizModel(model);
        AlipayTradeQueryResponse response = AliPayApi.execute(request);
        return response.getTradeStatus();
    }

    @SneakyThrows
    public void closeTrade(String outTradeNo) {
        AlipayTradeCloseModel alipayTradeCloseModel = new AlipayTradeCloseModel();
        alipayTradeCloseModel.setOutTradeNo(outTradeNo);
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        request.setBizModel(alipayTradeCloseModel);
        AliPayApi.doExecute(request);
    }


    /**
     * 支付成功回调
     *
     * @param request 请求参数
     * @param consumer 回调函数
     */
    public void payCallBack(HttpServletRequest request, Consumer<AliPayAsyncResponse> consumer) {
        Map<String, String> params = AliPayApi.toMap(request);
        AliPayAsyncResponse aliPayAsyncResponse = JSONUtil.toBean(JSONUtil.toJsonStr(params), AliPayAsyncResponse.class);
        // 回调
        consumer.accept(aliPayAsyncResponse);
    }



    public void sendPaySuccessEmail(Long userId,String email, String orderTotal, String totalAmount) {
        // 2. 构建内容对象
        Map<String, String> model = new HashMap<>();
        model.put("userId", Long.toString(userId));
        model.put("orderTotal", orderTotal);
        model.put("totalAmount", totalAmount);

        // 3. 通过 CompletableFuture 发送验证码
        CompletableFuture.runAsync(() -> {
            try {
                // 4. 获取模板内容
                Template template = TemplateUtil.createEngine(
                        new TemplateConfig("", TemplateConfig.ResourceMode.CLASSPATH)
                ).getTemplate("pay.html");
                String content = template.render(model);
                // 5. 发送邮件
                MailUtil.send(email, "APIMIX-充值通知", content, true);
            } catch (Exception e) {
                log.error("充值通知错误日志", e);
                throw new HorApiException("充值通知发送失败");
            }
        });
    }



}
