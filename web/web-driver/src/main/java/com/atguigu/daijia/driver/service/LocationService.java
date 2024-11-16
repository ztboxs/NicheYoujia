package com.atguigu.daijia.driver.service;

import com.atguigu.daijia.model.form.map.OrderServiceLocationForm;
import com.atguigu.daijia.model.form.map.UpdateDriverLocationForm;
import com.atguigu.daijia.model.form.map.UpdateOrderLocationForm;

import java.util.List;

public interface LocationService {


    /**
     * 开启接单服务：更新司机经纬度位置，远程调用
     * @param updateDriverLocationForm
     * @return
     */
    Boolean updateDriverLocation(UpdateDriverLocationForm updateDriverLocationForm);

    /**
     * 司机赶往起始地点-更新订单位置到 Redis 缓存
     * @param updateOrderLocationForm
     * @return
     */
    Boolean updateOrderLocationToCache(UpdateOrderLocationForm updateOrderLocationForm);

    /**
     * 开始代驾服务：保存代驾服务订单位置
     * @param orderLocationServiceFormList
     * @return
     */
    Boolean saveOrderServiceLocation(List<OrderServiceLocationForm> orderLocationServiceFormList);
}
