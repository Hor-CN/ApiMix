package cn.apimix.model.entity;


import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 日志实体
 *
 * @Author: Hor
 * @Date: 2024/8/10 下午1:20
 * @Version: 1.0
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "api_log")
public class ApiLog implements Serializable {

    /**
     * 主键ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;


    /**
     * 请求唯一ID
     */
    private String requestId;


    /**
     * 访问实例
     */
    private Long targetServer;

    /**
     * 实例名称
     */
    private String targetName;

    /**
     * 请求路径
     */
    private String requestPath;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 请求来源
     **/
    private String origin;

    /**
     * 请求ip
     */
    private String ip;

    /**
     * IP所属城市
     */
    private String city;

    /**
     * 请求用户ID
     */
    private Long userId;


    /**
     * 请求头
     */
    private String requestHeaders;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求体
     */
    private String requestBody;

    /**
     * 响应状态
     */
    private String status;

    /**
     * 响应头
     */
    private String responseHeaders;

    /**
     * 响应体
     */
    private String responseBody;


    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 请求时间
     */
    private Date requestTime;

    /**
     * 响应时间
     */
    private Date responseTime;

    /**
     * 创建时间
     */
    private Date createTime;

}
