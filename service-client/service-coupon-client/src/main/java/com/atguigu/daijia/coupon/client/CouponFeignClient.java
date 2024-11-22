package com.atguigu.daijia.coupon.client;

import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.model.form.coupon.UseCouponForm;
import com.atguigu.daijia.model.vo.coupon.AvailableCouponVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;


@FeignClient(value = "service-coupon")
public interface CouponFeignClient {


    /**
     * 领取优惠券
     * @param customerId
     * @param couponId
     * @return
     */
    @GetMapping("/coupon/info/receive/{customerId}/{couponId}")
    Result<Boolean> receive(@PathVariable("customerId") Long customerId, @PathVariable("couponId") Long couponId);

    /**
     * 获取未使用的最佳优惠券信息
     * @param customerId
     * @param orderAmount
     * @return
     */
    @GetMapping("/coupon/info/findAvailableCoupon/{customerId}/{orderAmount}")
    Result<List<AvailableCouponVo>> findAvailableCoupon(@PathVariable("customerId") Long customerId, @PathVariable("orderAmount") BigDecimal orderAmount);

    /**
     * 使用优惠券
     * @param useCouponForm
     * @return
     */
    @PostMapping("/coupon/info/useCoupon")
    Result<BigDecimal> useCoupon(@RequestBody UseCouponForm useCouponForm);

}