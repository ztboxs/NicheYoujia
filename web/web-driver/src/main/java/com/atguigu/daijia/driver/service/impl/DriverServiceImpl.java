package com.atguigu.daijia.driver.service.impl;

import com.atguigu.daijia.common.constant.RedisConstant;
import com.atguigu.daijia.common.execption.GuiguException;
import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.common.result.ResultCodeEnum;
import com.atguigu.daijia.driver.client.DriverInfoFeignClient;
import com.atguigu.daijia.driver.service.DriverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverInfoFeignClient driverInfoFeignClient;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 司机端登录接口实现(调用端)
     *
     * @param code
     * @return
     */
    @Override
    public String login(String code) {
        //远程调用，得到司机端Id
        Result<Long> longResult = driverInfoFeignClient.login(code);
        //todo判断Id是否为空
        Long driverId = longResult.getData();
        if (driverId == null || driverId.equals(0L)) {
            // 处理ID为空的情况
            throw new GuiguException(ResultCodeEnum.FAIL);
        }
        //token字符串
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        //放到redis，设置过期时间
        redisTemplate.opsForValue().set(RedisConstant.USER_LOGIN_KEY_PREFIX + token, driverId.toString(), RedisConstant.USER_LOGIN_KEY_TIMEOUT, TimeUnit.SECONDS);
        return token;
    }
}
