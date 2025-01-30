package cn.apimix.core.context;

import cn.apimix.core.utils.ExceptionUtils;
import cn.apimix.core.utils.IpUtils;
import cn.apimix.core.utils.ServletUtils;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户额外上下文
 *
 * @Author: Hor
 * @Date: 2024/12/16 11:45
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
public class UserExtraContext implements Serializable {

    /**
     * IP
     */
    private String ip;

    /**
     * IP 归属地
     */
    private String address;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    public UserExtraContext(HttpServletRequest request) {
        this.ip = ServletUtil.getClientIP(request);
        this.address = ExceptionUtils.exToNull(() -> IpUtils.getIpv4Address(this.ip));
        this.setBrowser(ServletUtils.getBrowser(request));
        this.setLoginTime(LocalDateTime.now());
        this.setOs(StrUtil.subBefore(ServletUtils.getOs(request), " or", false));
    }
}