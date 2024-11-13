package com.atguigu.daijia.driver.service;

import com.atguigu.daijia.model.vo.order.CurrentOrderInfoVo;
import com.atguigu.daijia.model.vo.order.NewOrderDataVo;
import com.atguigu.daijia.model.vo.order.OrderInfoVo;

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

    /**
     * 司机抢单
     * @param driverId
     * @param orderId
     * @return
     */
    Boolean robNewOrder(Long driverId, Long orderId);

    /**
     * 司机查找当前订单
     * @param driverId
     * @return
     */
    CurrentOrderInfoVo searchDriverCurrentOrder(Long driverId);

    /**
     * 获取订单账单详细信息
     * @param orderId
     * @param driverId
     * @return
     */
    OrderInfoVo getOrderInfo(Long orderId, Long driverId);
}
