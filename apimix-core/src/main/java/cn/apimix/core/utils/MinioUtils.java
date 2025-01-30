package cn.apimix.core.utils;

import cn.apimix.core.config.MinioConfig;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;

/**
 * MinIO操作工具类
 *
 * @Author: Hor
 * @Date: 2025/1/18 18:20
 * @Version: 1.0
 */
@Slf4j
@Component
public class MinioUtils {

    @Resource
    private MinioClient minioClient;

    @Resource
    private MinioConfig minioConfig;

    /**
     * 判断文件是否存在
     *
     * @param objectName 文件名称
     * @return Boolean
     */
    public boolean isObjectExist(String objectName) {
        boolean exist = true;
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(minioConfig.getBucketName()).object(objectName).build());
        } catch (Exception e) {
            log.error("[Minio工具类]>>>> 判断文件是否存在, 异常：", e);
            exist = false;
        }
        return exist;
    }

    /**
     * 判断文件夹是否存在
     *
     * @param objectName 文件夹名称
     * @return Boolean
     */
    public boolean isFolderExist(String objectName) {
        boolean exist = false;
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(minioConfig.getBucketName()).prefix(objectName).recursive(false).build());
            for (Result<Item> result : results) {
                Item item = result.get();
                if (item.isDir() && objectName.equals(item.objectName())) {
                    exist = true;
                }
            }
        } catch (Exception e) {
            log.error("[Minio工具类]>>>> 判断文件夹是否存在，异常：", e);
            exist = false;
        }
        return exist;
    }

    /**
     * 获取文件流
     *
     * @param objectName 文件名
     * @return 二进制流
     */
    @SneakyThrows(Exception.class)
    public InputStream getObject(String objectName) {
        return minioClient.getObject(GetObjectArgs.builder().bucket(minioConfig.getBucketName()).object(objectName).build());
    }

    /**
     * 上传文件
     *
     * @param objectName 对象名称
     * @param file       文件
     */
    @SneakyThrows(Exception.class)
    public void uploadFile(String objectName, MultipartFile file) {
        InputStream inputStream = file.getInputStream();
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(objectName)
                .stream(inputStream, file.getSize(), -1)
                .contentType(file.getContentType())
                .build());
        inputStream.close();
    }


    /**
     * 获取文件信息, 如果抛出异常则说明文件不存在
     *
     * @param objectName 文件名称
     * @return String
     */
    @SneakyThrows(Exception.class)
    public String getFileStatusInfo(String objectName) {
        return minioClient.statObject(StatObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(objectName)
                .build()).toString();
    }

    /**
     * 删除文件
     *
     * @param objectName 文件名称
     */
    @SneakyThrows(Exception.class)
    public void removeFile(String objectName) {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(objectName)
                .build());
    }


    /**
     * 获取文件外链
     *
     * @param objectName 文件名
     * @param expires    过期时间 <=7 秒 （外链有效时间（单位：秒））
     * @return url
     */
    @SneakyThrows(Exception.class)
    public String getPresignedObjectUrl(String objectName, Integer expires) {
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .expiry(expires)
                .bucket(minioConfig.getBucketName())
                .object(objectName).build();
        return minioClient.getPresignedObjectUrl(args);
    }

    /**
     * 获得文件外链
     *
     * @param objectName 文件
     * @return url
     */
    @SneakyThrows(Exception.class)
    public String getPresignedObjectUrl(String objectName) {
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(objectName)
                .method(Method.GET).build();
        return minioClient.getPresignedObjectUrl(args);
    }



}
