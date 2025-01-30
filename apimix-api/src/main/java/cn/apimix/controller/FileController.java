package cn.apimix.controller;

import cn.apimix.core.annotation.ResponseResult;
import cn.apimix.core.utils.MinioUtils;
import cn.dev33.satoken.annotation.SaCheckLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 文件控制器
 *
 * @Author: Hor
 * @Date: 2025/1/18 18:47
 * @Version: 1.0
 */
@Slf4j
@RestController
@ResponseResult
@RequestMapping("/api/file")
public class FileController {

    @Resource
    private MinioUtils minioUtils;


    @SaCheckLogin
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        // 判断文件是否为空
        if (file == null || file.getSize() == 0) {
            log.error("==> 上传文件异常：文件大小为空 ...");
            throw new RuntimeException("文件大小不能为空");
        }
        // 文件的原始名称
        String originalFileName = file.getOriginalFilename();
        // 生成存储对象的名称（将 UUID 字符串中的 - 替换成空字符串）
        String key = UUID.randomUUID().toString().replace("-", "");
        // 获取文件的后缀，如 .jpg
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));

        // 拼接上文件后缀，即为要存储的文件名
        String objectName = String.format("%s%s", key, suffix);

        log.info("==> 开始上传文件至 Minio, ObjectName: {}", objectName);

        // 上传
        minioUtils.uploadFile(objectName, file);
        // 获取文件外链
        return minioUtils.getPresignedObjectUrl(objectName);
    }
}
