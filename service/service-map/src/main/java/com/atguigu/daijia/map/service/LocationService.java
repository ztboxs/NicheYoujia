package com.atguigu.daijia.map.service;

import com.atguigu.daijia.model.form.map.OrderServiceLocationForm;
import com.atguigu.daijia.model.form.map.SearchNearByDriverForm;
import com.atguigu.daijia.model.form.map.UpdateDriverLocationForm;
import com.atguigu.daijia.model.form.map.UpdateOrderLocationForm;
import com.atguigu.daijia.model.vo.map.NearByDriverVo;
import com.atguigu.daijia.model.vo.map.OrderLocationVo;
import com.atguigu.daijia.model.vo.map.OrderServiceLastLocationVo;

import java.math.BigDecimal;
import java.util.List;

public interface LocationService {

    /**
     * 开启接单服务：更新司机经纬度位置接口
     * @param updateDriverLocationForm
     * @return
     */
    Boolean updateDriverLocation(UpdateDriverLocationForm updateDriverLocationForm);


    /**
     * 关闭接单服务：删除司机经纬度位置即可
     * @param driverId
     * @return
     */
    Boolean removeDriverLocation(Long driverId);

    /**
     * 搜索附近满足条件的司机
     * @param searchNearByDriverForm
     * @return
     */
    List<NearByDriverVo> searchNearByDriver(SearchNearByDriverForm searchNearByDriverForm);

    /**
     * 司机赶往代驾起始地点：更新订单地址到缓存
     * @param updateOrderLocationForm
     * @return
     */
    Boolean updateOrderLocationToCache(UpdateOrderLocationForm updateOrderLocationForm);

    /**
     * 司机赶往起始代驾地点：获取订单经纬度信息
     * @param orderId
     * @return
     */
    OrderLocationVo getCacheOrderLocation(Long orderId);

    /**
     * 批量保存代驾服务订单位置
     * @param orderLocationServiceFormList
     * @return
     */
    Boolean saveOrderServiceLocation(List<OrderServiceLocationForm> orderLocationServiceFormList);

    /**
     * 代驾服务：获取订单服务的最后一个位置信息
     * @param orderId
     * @return
     */
    OrderServiceLastLocationVo getOrderServiceLastLocation(Long orderId);

    /**
     * 代驾服务：计算订单实际里程
     * @param orderId
     * @return
     */
    BigDecimal calculateOrderRealDistance(Long orderId);
}
