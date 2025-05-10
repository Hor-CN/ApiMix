package cn.apimix.user.service.impl;

import cn.apimix.core.config.RedisKeyConstants;
import cn.apimix.core.exception.HorApiException;
import cn.apimix.core.utils.RedisUtils;
import cn.apimix.user.model.resp.CaptchaResp;
import cn.apimix.user.service.CaptchaService;
import cn.dev33.satoken.util.SaFoxUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateUtil;
import com.wf.captcha.SpecCaptcha;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @Author: Hor
 * @Date: 2024/5/23 21:46
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {

    private final TaskExecutor myThreadPool;

    private static final String CAPTCHA_EXPIRED = "验证码已失效";
    private static final String CAPTCHA_ERROR = "验证码错误";

    /**
     * 发送邮件验证码
     *
     * @param email 邮件
     */
    @Override
    public void sendEmailCode(String email, Duration duration) {
        // 1. 获取验证码
        String code = SaFoxUtil.getRandomString(4).toLowerCase();
        // 2. 构建内容对象
        Map<String, String> model = new HashMap<>();
        model.put("code", code);
        model.put("time", String.valueOf(duration.toMinutes()));
        // 3. 通过 CompletableFuture 发送验证码
        CompletableFuture.runAsync(() -> {
            try {
                // 4. 获取模板内容
                Template template = TemplateUtil.createEngine(
                        new TemplateConfig("", TemplateConfig.ResourceMode.CLASSPATH)
                ).getTemplate("sendCode.html");
                String content = template.render(model);
                // 5. 发送邮件
                MailUtil.send(email, "APIMIX邮箱验证", content, true);
            } catch (Exception e) {
                log.error("验证码错误日志", e);
                throw new HorApiException("验证码发送失败");
            }
        }, myThreadPool).thenRunAsync(() -> {
            // 6. 存入 Redis
            String captchaKey = RedisKeyConstants.CAPTCHA_KEY_PREFIX + email;
            RedisUtils.set(captchaKey, code, duration);
        }, myThreadPool);
    }

    /**
     * 校验验证码
     *
     * @param email 邮箱
     * @param code  验证码
     */
    @Override
    public void checkEmailCode(String email, String code) {
        String captchaKey = RedisKeyConstants.CAPTCHA_KEY_PREFIX + email;
        String captcha = RedisUtils.get(captchaKey);
        // 验证码已失效
        Assert.notBlank(captcha, CAPTCHA_EXPIRED);
        // 验证码是否相等
        Assert.isTrue(captcha.equalsIgnoreCase(code), CAPTCHA_ERROR);
        RedisUtils.delete(captchaKey);
    }

    /**
     * 获取图片验证码
     *
     * @return 验证码对象
     */
    @Override
    public CaptchaResp getImageCaptcha() {
        String uuid = IdUtil.fastUUID();
        String captchaKey = RedisKeyConstants.CAPTCHA_KEY_PREFIX + uuid;
        SpecCaptcha specCaptcha = new SpecCaptcha(110, 36, 4);
        long expireTime = LocalDateTimeUtil.toEpochMilli(
                LocalDateTime.now().plusMinutes(5)
        );
        // 存入redis并设置过期时间为5分钟
        RedisUtils.set(captchaKey, specCaptcha.text(), Duration.ofMinutes(5));
        return CaptchaResp.builder()
                .uuid(uuid)
                .img(specCaptcha.toBase64())
                .expireTime(expireTime)
                .build();
    }

    /**
     * 校验图片验证码
     *
     * @param uuid    唯一ID
     * @param captcha 验证码
     */
    @Override
    public void checkImageCaptcha(String uuid, String captcha) {
        // 验证码前缀
        String captchaKey = RedisKeyConstants.CAPTCHA_KEY_PREFIX + uuid;
        // 获取验证码
        String code = RedisUtils.get(captchaKey);
        // 验证码已失效
        Assert.notBlank(captcha, "验证码已失效");
        // 验证码是否相等
        Assert.isTrue(captcha.equalsIgnoreCase(code), "验证码错误");
        // 验证成功删除
        RedisUtils.delete(captchaKey);
    }


}
