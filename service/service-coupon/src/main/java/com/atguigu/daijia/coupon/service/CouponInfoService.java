package com.atguigu.daijia.coupon.service;

import com.atguigu.daijia.model.entity.coupon.CouponInfo;
import com.atguigu.daijia.model.vo.coupon.AvailableCouponVo;
import com.baomidou.mybatisplus.extension.service.IService;
import java.math.BigDecimal;
import java.util.List;

public interface CouponInfoService extends IService<CouponInfo> {


    /**
     * 领取优惠券
     * @param customerId
     * @param couponId
     * @return
     */
    Boolean receive(Long customerId, Long couponId);

    /**
     * 获取未使用的最佳优惠卷
     * @param customerId
     * @param orderAmount
     * @return
     */
    List<AvailableCouponVo> findAvailableCoupon(Long customerId, BigDecimal orderAmount);
}
