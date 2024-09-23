package com.atguigu.daijia.customer.service;

import com.atguigu.daijia.model.form.customer.UpdateWxPhoneForm;
import com.atguigu.daijia.model.vo.customer.CustomerLoginVo;

public interface CustomerService {


    /**
     * 微信登录
     * @param code
     * @return
     */
    String login(String code);

    /**
     * 获取用户登录信息
     * @param token
     * @return
     */
    CustomerLoginVo getCustomerLoginInfo(String token);

    /**
     * 更新用户手机号接口
     * @param updateWxPhoneForm
     * @return
     */
    Boolean updateWxPhoneNumber(UpdateWxPhoneForm updateWxPhoneForm);
}
