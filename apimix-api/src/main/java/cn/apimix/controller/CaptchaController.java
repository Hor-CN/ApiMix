package cn.apimix.controller;

import cn.apimix.common.resp.Result;
import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.model.vo.CaptchaResp;
import cn.apimix.service.impl.CaptchaServiceImpl;
import cn.hutool.core.lang.RegexPool;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.Duration;

/**
 * 验证码
 *
 * @Author: Hor
 * @Date: 2024/8/27 下午8:39
 * @Version: 1.0
 */
@RestController
@ResponseResult
@RequestMapping("/api/captcha")
public class CaptchaController {

    @Resource
    private CaptchaServiceImpl captchaService;

    /**
     * 获取邮箱验证码
     *
     * <p>
     * 限流规则：<br>
     * 1.同一邮箱同一模板，1分钟2条，1小时8条，24小时20条 <br>
     * 2、同一邮箱所有模板 24 小时 100 条 <br>
     * 3、同一 IP 每分钟限制发送 30 条
     * </p>
     *
     * @param email 邮箱
     * @return /
     */
    @GetMapping("/mail")
    public Result<String> getMailCaptcha(
            @NotBlank(message = "邮箱不能为空")
            @Pattern(regexp = RegexPool.EMAIL, message = "邮箱格式错误")
            String email) {
        captchaService.sendEmailCode(email, Duration.ofMinutes(5));
        return Result.buildSuccess(String.format("发送成功，验证码有效期 %s 分钟", 5));
    }


    @GetMapping("/image")
    public CaptchaResp getImageCaptcha() {
        return captchaService.getImageCaptcha();
    }

}
