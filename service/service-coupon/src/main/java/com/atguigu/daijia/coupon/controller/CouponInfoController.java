package com.atguigu.daijia.coupon.controller;

import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.coupon.service.CouponInfoService;
import com.atguigu.daijia.model.form.coupon.UseCouponForm;
import com.atguigu.daijia.model.vo.coupon.AvailableCouponVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@Tag(name = "优惠券活动接口管理")
@RestController
@RequestMapping(value="/coupon/info")
@SuppressWarnings({"unchecked", "rawtypes"})
public class CouponInfoController {


    @Autowired
    private CouponInfoService couponInfoService;

    @Operation(summary = "领取优惠券")
    @GetMapping("/receive/{customerId}/{couponId}")
    public Result<Boolean> receive(@PathVariable Long customerId, @PathVariable Long couponId) {
        return Result.ok(couponInfoService.receive(customerId, couponId));
    }

    @Operation(summary = "获取未使用的最佳优惠券信息")
    @GetMapping("/findAvailableCoupon/{customerId}/{orderAmount}")
    public Result<List<AvailableCouponVo>> findAvailableCoupon(@PathVariable Long customerId, @PathVariable BigDecimal orderAmount) {
        return Result.ok(couponInfoService.findAvailableCoupon(customerId, orderAmount));
    }

    @Operation(summary = "使用优惠券")
    @PostMapping("/useCoupon")
    public Result<BigDecimal> useCoupon(@RequestBody UseCouponForm useCouponForm) {
        return Result.ok(couponInfoService.useCoupon(useCouponForm));
    }

}

