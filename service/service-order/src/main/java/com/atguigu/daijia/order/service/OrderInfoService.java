package com.atguigu.daijia.order.service;

import com.atguigu.daijia.model.entity.order.OrderInfo;
import com.atguigu.daijia.model.form.order.OrderInfoForm;
import com.atguigu.daijia.model.form.order.UpdateOrderCartForm;
import com.atguigu.daijia.model.vo.order.CurrentOrderInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrderInfoService extends IService<OrderInfo> {

    /**
     * 保存订单信息接口
     * @param orderInfoForm
     * @return
     */
    Long saveOrderInfo(OrderInfoForm orderInfoForm);

    /**
     * 更加订单Id获取订单状态
     * @param orderId
     * @return
     */
    Integer getOrderStatus(Long orderId);

    /**
     * 司机抢单
     * @param driverId
     * @param orderId
     * @return
     */
    Boolean robNewOrder(Long driverId, Long orderId);

    /**
     * 乘客端查找当前订单
     * @param customerId
     * @return
     */
    CurrentOrderInfoVo searchCustomerCurrentOrder(Long customerId);

    /**
     * 司机端查找当前订单
     * @param driverId
     * @return
     */
    CurrentOrderInfoVo searchDriverCurrentOrder(Long driverId);

    /**
     * 司机到达起始点
     * @param orderId
     * @param driverId
     * @return
     */
    Boolean driverArriveStartLocation(Long orderId, Long driverId);

    /**
     * 更新代驾车辆信息
     * @param updateOrderCartForm
     * @return
     */
    Boolean updateOrderCart(UpdateOrderCartForm updateOrderCartForm);
}
