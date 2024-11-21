package com.atguigu.daijia.coupon.mapper;

import com.atguigu.daijia.model.entity.coupon.CustomerCoupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerCouponMapper extends BaseMapper<CustomerCoupon> {


    /**
     * 更新领取数量
     * @param couponId
     * @return
     */
    int updateReceiveCount(Long couponId);
}
