package com.atguigu.daijia.customer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 天天进步
 *
 * @Author: ztbox
 * @Date: 2024/09/23/16:15
 * @Description: 小程序ID、密钥配置类
 */
@Component
@Data
@ConfigurationProperties(prefix = "wx.miniapp")
public class WxConfigProperties {
    private String appid;
    private String secret;
}
