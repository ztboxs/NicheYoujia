package com.atguigu.daijia.driver.service;

import com.atguigu.daijia.model.form.map.UpdateDriverLocationForm;
import com.atguigu.daijia.model.form.map.UpdateOrderLocationForm;

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
}
