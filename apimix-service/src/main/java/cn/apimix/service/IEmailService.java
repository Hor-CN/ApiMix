package cn.apimix.service;

/**
 * @Author: Hor
 * @Date: 2024/5/23 21:45
 * @Version: 1.0
 */
public interface IEmailService {

    /**
     * 发送邮件验证码
     *
     * @param email 邮件
     * @return boolean
     */
    Boolean sendEmailCode(String email);
}
