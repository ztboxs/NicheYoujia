package com.atguigu.daijia.coupon.service.impl;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.atguigu.daijia.common.execption.GuiguException;
import com.atguigu.daijia.common.result.ResultCodeEnum;
import com.atguigu.daijia.coupon.mapper.CouponInfoMapper;
import com.atguigu.daijia.coupon.mapper.CustomerCouponMapper;
import com.atguigu.daijia.coupon.service.CouponInfoService;
import com.atguigu.daijia.model.entity.coupon.CouponInfo;
import com.atguigu.daijia.model.entity.coupon.CustomerCoupon;
import com.atguigu.daijia.model.vo.coupon.AvailableCouponVo;
import com.atguigu.daijia.model.vo.coupon.NoUseCouponVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class CouponInfoServiceImpl extends ServiceImpl<CouponInfoMapper, CouponInfo> implements CouponInfoService {


    @Autowired
    private CouponInfoMapper couponInfoMapper;
    @Autowired
    private CustomerCouponMapper customerCouponMapper;

    /**
     * 领取优惠券
     *
     * @param customerId
     * @param couponId
     * @return
     */
    @Override
    public Boolean receive(Long customerId, Long couponId) {
        //1.CouponId查询优惠卷信息
        //判断优惠券是否存在
        CouponInfo couponInfo = couponInfoMapper.selectById(couponId);
        if (couponInfo == null) {
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }

        //2.判断优惠券是否过期
        if (couponInfo.getExpireTime().before(new Date())) {
            throw new GuiguException(ResultCodeEnum.COUPON_EXPIRE);
        }

        //3.检查库存、发行数量和领取数量
        if (couponInfo.getPublishCount() != 0 && couponInfo.getReceiveCount() == couponInfo.getPublishCount()) {
            throw new GuiguException(ResultCodeEnum.COUPON_LESS);
        }

        //4.检查每个人限领取的数量
        if (couponInfo.getPerLimit() > 0) {
            //统计当前客户已经领优惠卷数量
            LambdaQueryWrapper<CustomerCoupon> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CustomerCoupon::getCouponId, couponId);
            wrapper.eq(CustomerCoupon::getCustomerId, customerId);
            Long count = customerCouponMapper.selectCount(wrapper);
            //判断
            if (count > couponInfo.getPerLimit()) {
                throw new GuiguException(ResultCodeEnum.COUPON_USER_LIMIT);
            }
        }
        //5.领取优惠卷
        //5.1 更新领取数量
        int row = customerCouponMapper.updateReceiveCount(couponId);

        //5.2添加领取记录
        this.saveCustomerCoupon(customerId, couponId, couponInfo.getExpireTime());

        return true;
    }

    /**
     * 获取未使用的最佳优惠券信息
     *
     * @param customerId
     * @param orderAmount
     * @return
     */
    @Override
    public List<AvailableCouponVo> findAvailableCoupon(Long customerId, BigDecimal orderAmount) {

        //1 创建list集合，存储最终返回数据
        List<AvailableCouponVo> availableCouponVoList = new ArrayList<>();

        //2 根据乘客id，获取乘客已经领取但是没有使用的优惠卷列表
        //返回list集合
        List<NoUseCouponVo> list = couponInfoMapper.findNoUseList(customerId);

        //3 遍历乘客未使用优惠卷列表，得到每个优惠卷
        //3.1 判断优惠卷类型：现金卷 和 折扣卷
        List<NoUseCouponVo> typeList =
                list.stream().filter(item -> item.getCouponType() == 1).collect(Collectors.toList());

        //3.2 是现金券
        //判断现金卷是否满足条件
        for (NoUseCouponVo noUseCouponVo : typeList) {
            //判断使用门槛
            //减免金额
            BigDecimal reduceAmount = noUseCouponVo.getAmount();
            //1 没有门槛  == 0，订单金额必须大于优惠减免金额
            if (noUseCouponVo.getConditionAmount().doubleValue() == 0
                    && orderAmount.subtract(reduceAmount).doubleValue() > 0) {
                availableCouponVoList.add(this.buildBestNoUseCouponVo(noUseCouponVo, reduceAmount));
            }

            //2 有门槛  ，订单金额大于优惠门槛金额
            if (noUseCouponVo.getConditionAmount().doubleValue() > 0
                    && orderAmount.subtract(noUseCouponVo.getConditionAmount()).doubleValue() > 0) {
                availableCouponVoList.add(this.buildBestNoUseCouponVo(noUseCouponVo, reduceAmount));
            }
        }

        //3.3 折扣卷
        //判断折扣卷是否满足条件
        List<NoUseCouponVo> typeList2 =
                list.stream().filter(item -> item.getCouponType() == 2).collect(Collectors.toList());
        for (NoUseCouponVo noUseCouponVo : typeList2) {
            //折扣之后金额
            // 100 打8折  = 100 * 8 /10= 80
            BigDecimal discountAmount = orderAmount.multiply(noUseCouponVo.getDiscount())
                    .divide(new BigDecimal("10")).setScale(2, RoundingMode.HALF_UP);

            BigDecimal reduceAmount = orderAmount.subtract(discountAmount);
            //2.2.1.没门槛
            if (noUseCouponVo.getConditionAmount().doubleValue() == 0) {
                availableCouponVoList.add(this.buildBestNoUseCouponVo(noUseCouponVo, reduceAmount));
            }
            //2.2.2.有门槛，订单折扣后金额大于优惠券门槛金额
            if (noUseCouponVo.getConditionAmount().doubleValue() > 0
                    && discountAmount.subtract(noUseCouponVo.getConditionAmount()).doubleValue() > 0) {
                availableCouponVoList.add(this.buildBestNoUseCouponVo(noUseCouponVo, reduceAmount));
            }

        }

        //4 把满足条件优惠卷放到最终list集合
        //根据金额排序
        if (!CollectionUtils.isEmpty(availableCouponVoList)) {
            Collections.sort(availableCouponVoList, new Comparator<AvailableCouponVo>() {
                @Override
                public int compare(AvailableCouponVo o1, AvailableCouponVo o2) {
                    return o1.getReduceAmount().compareTo(o2.getReduceAmount());
                }
            });
        }

        return availableCouponVoList;
    }


    private AvailableCouponVo buildBestNoUseCouponVo(NoUseCouponVo noUseCouponVo, BigDecimal reduceAmount) {
        AvailableCouponVo bestNoUseCouponVo = new AvailableCouponVo();
        BeanUtils.copyProperties(noUseCouponVo, bestNoUseCouponVo);
        bestNoUseCouponVo.setCouponId(noUseCouponVo.getId());
        bestNoUseCouponVo.setReduceAmount(reduceAmount);
        return bestNoUseCouponVo;
    }


    //添加领取记录
    private void saveCustomerCoupon(Long customerId, Long couponId, Date expireTime) {
        CustomerCoupon customerCoupon = new CustomerCoupon();
        customerCoupon.setCustomerId(customerId);
        customerCoupon.setCouponId(couponId);
        customerCoupon.setExpireTime(expireTime);
        customerCoupon.setReceiveTime(new Date());
        customerCoupon.setStatus(1);
        customerCouponMapper.insert(customerCoupon);
    }
}
