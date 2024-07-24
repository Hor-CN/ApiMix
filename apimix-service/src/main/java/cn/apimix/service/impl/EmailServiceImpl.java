package cn.apimix.service.impl;

import cn.apimix.service.IEmailService;
import cn.dev33.satoken.util.SaFoxUtil;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Hor
 * @Date: 2024/5/23 21:46
 * @Version: 1.0
 */
@Service
public class EmailServiceImpl implements IEmailService {

    /**
     * 发送邮件验证码
     *
     * @param email 邮件
     * @return boolean
     */
    @Override
    public Boolean sendEmailCode(String email) {
        String code = SaFoxUtil.getRandomString(4);
        Map<String, String> model = new HashMap<>();
        model.put("code", code);
        // 获取模板内容
        Template template = TemplateUtil.createEngine(new TemplateConfig("", TemplateConfig.ResourceMode.CLASSPATH)).getTemplate("sendCode.html");
        String content = template.render(model);
        try {
            MailUtil.send(email, "APIMIX邮箱验证", content, true);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
