package cn.apimix.comment.util;

import cn.apimix.core.constant.StringConstants;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HtmlUtil;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import net.dreamlu.mica.ip2region.core.IpInfo;

import java.util.Objects;
import java.util.Set;

/**
 * @Author: Hor
 * @Date: 2025/1/31 00:43
 * @Version: 1.0
 */
public class IpUtil {

    /**
     * 查询 IP 归属地（本地库解析）
     *
     * @param ip IP 地址
     * @return IP 归属地
     */
    public static String getIpv4Address(String ip, Ip2regionSearcher ip2regionSearcher) {
        if (!isInnerIpv4(ip)) {
            IpInfo ipInfo = ip2regionSearcher.memorySearch(ip);
            if (null == ipInfo) {
                return "未知";
            }
            Set<String> regionSet = CollUtil.newLinkedHashSet(ipInfo.getCountry(), ipInfo.getRegion(), ipInfo
                    .getProvince());
            regionSet.removeIf(Objects::isNull);
            return String.join(StringConstants.PIPE, regionSet);
        } else {
            return "内网";
        }
    }

    /**
     * 是否为内网 IPv4
     *
     * @param ip IP 地址
     * @return 是否为内网 IP
     */
    public static boolean isInnerIpv4(String ip) {
        return NetUtil.isInnerIP("0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : HtmlUtil.cleanHtmlTag(ip));
    }
}
