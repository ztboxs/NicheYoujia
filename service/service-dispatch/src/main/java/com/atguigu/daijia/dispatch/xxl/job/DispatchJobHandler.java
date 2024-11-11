package com.atguigu.daijia.dispatch.xxl.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * 天天进步
 *
 * @Author: ztbox
 * @Date: 2024/10/21/19:29
 * @Description: xxl-job测试类
 */
@Component
public class DispatchJobHandler {

    @XxlJob("firstJobHandler")
    public void testJobHandler() {
        System.out.println("xxl-job项目集成测试");
    }
}
