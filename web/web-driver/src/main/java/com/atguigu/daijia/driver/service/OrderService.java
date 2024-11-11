package com.atguigu.daijia.driver.service;

import com.atguigu.daijia.model.vo.order.NewOrderDataVo;

import java.util.List;

public interface OrderService {


    /**
     * 查询订单状态
     * @param orderId
     * @return
     */
    Integer getOrderStatus(Long orderId);

    /**
     * 查询司机新订单数据
     * @param driverId
     * @return
     */
    List<NewOrderDataVo> findNewOrderQueueData(Long driverId);
}
