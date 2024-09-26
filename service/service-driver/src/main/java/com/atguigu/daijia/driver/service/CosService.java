package com.atguigu.daijia.driver.service;

import com.atguigu.daijia.model.vo.driver.CosUploadVo;
import org.springframework.web.multipart.MultipartFile;

public interface CosService {


    /**
     * 腾讯云文件上传
     * @param file
     * @param path
     * @return
     */
    CosUploadVo upload(MultipartFile file, String path);

    /**
     * 获取临时签名URL接口
     * @param path
     * @return
     */
    String getImageUrl(String path);
}
