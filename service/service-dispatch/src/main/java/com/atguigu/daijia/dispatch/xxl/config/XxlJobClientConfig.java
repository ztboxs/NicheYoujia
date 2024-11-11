package com.atguigu.daijia.dispatch.xxl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 天天进步
 *
 * @Author: ztbox
 * @Date: 2024/10/22/22:10
 * @Description: 配置类,读取配置文件里面调用的调度中心任务
 */
@Data
@Component
@ConfigurationProperties(prefix = "xxl.job.client")
public class XxlJobClientConfig {

    private Integer jobGroupId;
    private String addUrl;
    private String removeUrl;
    private String startJobUrl;
    private String stopJobUrl;
    private String addAndStartUrl;
}
