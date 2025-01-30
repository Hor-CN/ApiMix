package cn.apimix.core.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Minio config
 *
 * @Author: Hor
 * @Date: 2025/1/18 17:53
 * @Version: 1.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

    /**
     * 服务地址
     */
    private String endpoint;

    private String accessKey;
    private String secretKey;

    /**
     * 存储桶名称
     */
    private String bucketName;


    /**
     * 初始化MinIO客户端
     *
     * @return MinioClient
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

}
