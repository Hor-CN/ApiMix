package cn.apimix.controller;

import cn.apimix.RedisUtils;
import cn.apimix.common.resp.Result;
import cn.apimix.config.CacheConstants;
import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.model.vo.CaptchaResp;
import cn.apimix.service.impl.EmailServiceImpl;
import cn.dev33.satoken.util.SaFoxUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.RegexPool;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
    private EmailServiceImpl emailService;


    @Async("threadPool")
    @RequestMapping("/emailCode")
    public void getEmailCaptcha(@RequestParam String email) {
        emailService.sendEmailCode(email);
    }

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
    public Result<String> getMailCaptcha(@NotBlank(message = "邮箱不能为空") @Pattern(regexp = RegexPool.EMAIL, message = "邮箱格式错误") String email) {
        String code = SaFoxUtil.getRandomString(4);
        Map<String, String> model = new HashMap<>();
        model.put("code", code);
        // 获取模板内容
        Template template = TemplateUtil.createEngine(new TemplateConfig("", TemplateConfig.ResourceMode.CLASSPATH)).getTemplate("sendCode.html");
        String content = template.render(model);
        try {
            MailUtil.send(email, "APIMIX邮箱验证", content, true);
            String captchaKey = CacheConstants.CAPTCHA_KEY_PREFIX + email;
            // 保存验证码
            RedisUtils.set(captchaKey, code, Duration.ofMinutes(5));
            return Result.buildSuccess(String.format("发送成功，验证码有效期 %s 分钟", 5));
        } catch (Exception e) {
            return Result.buildFail("验证码发送失败");
        }


    }


    @GetMapping("/image")
    public CaptchaResp getImageCaptcha() {
        String uuid = IdUtil.fastUUID();
        String captchaKey = CacheConstants.CAPTCHA_KEY_PREFIX + uuid;
        CircleCaptcha lineCaptcha = CaptchaUtil.createCircleCaptcha(110, 36, 4, 10);
        long expireTime = LocalDateTimeUtil.toEpochMilli(LocalDateTime.now()
                .plusMinutes(5));
        RedisUtils.set(captchaKey, lineCaptcha.getCode(), Duration.ofMinutes(5));
        return CaptchaResp.builder().uuid(uuid).img(lineCaptcha.getImageBase64Data()).expireTime(expireTime).build();

    }

}
