package com.atguigu.daijia.driver.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    /**
     * minio 上传文件
     * @param file
     * @return
     */
    String upload(MultipartFile file);
}
