package com.atguigu.daijia.customer.service;

import com.atguigu.daijia.model.form.customer.ExpectOrderForm;
import com.atguigu.daijia.model.form.customer.SubmitOrderForm;
import com.atguigu.daijia.model.vo.customer.ExpectOrderVo;
import com.atguigu.daijia.model.vo.order.CurrentOrderInfoVo;
import com.atguigu.daijia.model.vo.order.OrderInfoVo;

public interface OrderService {

    /**
     * 预估订单数据
     * @param expectOrderForm
     * @return
     */
    ExpectOrderVo expectOrder(ExpectOrderForm expectOrderForm);

    /**
     * 乘客下单接口
     * @param submitOrderForm
     * @return
     */
    Long submitOrder(SubmitOrderForm submitOrderForm);

    /**
     * 查询订单状态
     * @param orderId
     * @return
     */
    Integer getOrderStatus(Long orderId);

    /**
     * 乘客查找当前订单
     * @param customerId
     * @return
     */
    CurrentOrderInfoVo searchCustomerCurrentOrder(Long customerId);

    /**
     * 获取订单信息
     * @param orderId
     * @param customerId
     * @return
     */
    OrderInfoVo getOrderInfo(Long orderId, Long customerId);
}
