package cn.apimix.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: Hor
 * @Date: 2025/1/18 18:04
 * @Version: 1.0
 */
public interface MinioService {

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 文件地址
     */
    String uploadFile(MultipartFile file);



}
