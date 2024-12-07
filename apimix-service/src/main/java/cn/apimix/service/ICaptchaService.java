package cn.apimix.service;

import cn.apimix.model.vo.CaptchaResp;

import java.time.Duration;

/**
 * @Author: Hor
 * @Date: 2024/5/23 21:45
 * @Version: 1.0
 */
public interface ICaptchaService {

    /**
     * 发送邮件验证码
     *
     * @param email    邮件
     * @param duration 验证码有效期
     */
    void sendEmailCode(String email, Duration duration);

    /**
     * 校验验证码
     *
     * @param email 邮箱
     * @param code  验证码
     */
    void checkEmailCode(String email, String code);

    /**
     * 获取图片验证码
     *
     * @return 验证码对象
     */
    CaptchaResp getImageCaptcha();

    /**
     * 校验图片验证码
     *
     * @param uuid    唯一ID
     * @param captcha 验证码
     */
    void checkImageCaptcha(String uuid, String captcha);
}
