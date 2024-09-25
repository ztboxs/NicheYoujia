package com.atguigu.daijia.driver.service;

import com.atguigu.daijia.model.vo.driver.IdCardOcrVo;
import org.springframework.web.multipart.MultipartFile;

public interface OcrService {


    /**
     * 腾讯云身份证识别接口
     * @param file
     * @return
     */
    IdCardOcrVo idCardOcr(MultipartFile file);
}
