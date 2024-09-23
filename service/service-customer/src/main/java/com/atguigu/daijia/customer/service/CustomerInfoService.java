package com.atguigu.daijia.customer.service;

import com.atguigu.daijia.model.entity.customer.CustomerInfo;
import com.atguigu.daijia.model.form.customer.UpdateWxPhoneForm;
import com.atguigu.daijia.model.vo.customer.CustomerLoginVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CustomerInfoService extends IService<CustomerInfo> {

    /**
     * 微信小程序登录接口
     * @param code
     * @return
     */
    Long login(String code);

    /**
     *获取客户基本信息接口
     * @param customerId
     * @return
     */
    CustomerLoginVo getCustomerInfo(Long customerId);

    /**
     * 更新客户微信手机号码接口
     * @param updateWxPhoneForm
     * @return
     */
    Boolean updateWxPhoneNumber(UpdateWxPhoneForm updateWxPhoneForm);
}
