package cn.apimix.service.impl;

import cn.apimix.service.MinioService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: Hor
 * @Date: 2025/1/18 18:17
 * @Version: 1.0
 */
@Service
public class MinioServiceImpl implements MinioService {

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 文件地址
     */
    @Override
    public String uploadFile(MultipartFile file) {
        return "";
    }
}
