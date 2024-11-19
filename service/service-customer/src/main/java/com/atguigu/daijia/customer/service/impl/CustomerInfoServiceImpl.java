package com.atguigu.daijia.customer.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.atguigu.daijia.common.execption.GuiguException;
import com.atguigu.daijia.common.result.ResultCodeEnum;
import com.atguigu.daijia.customer.mapper.CustomerInfoMapper;
import com.atguigu.daijia.customer.mapper.CustomerLoginLogMapper;
import com.atguigu.daijia.customer.service.CustomerInfoService;
import com.atguigu.daijia.model.entity.customer.CustomerInfo;
import com.atguigu.daijia.model.entity.customer.CustomerLoginLog;
import com.atguigu.daijia.model.form.customer.UpdateWxPhoneForm;
import com.atguigu.daijia.model.vo.customer.CustomerLoginVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class CustomerInfoServiceImpl extends ServiceImpl<CustomerInfoMapper, CustomerInfo> implements CustomerInfoService {


    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private CustomerInfoMapper customerInfoMapper;

    @Autowired
    private CustomerLoginLogMapper customerLoginLogMapper;

    /**
     * 小程序登录接口
     *
     * @param code
     * @return
     */
    @Override
    public Long login(String code) {
        //1 获取code值，使用微信工具包对象，获取微信唯一标识openid
        String openid = null;
        try {
            WxMaJscode2SessionResult sessionInfo = wxMaService.getUserService().getSessionInfo(code);
            //优化一用Optional来优雅地处理可能的null值
            openid = Optional.ofNullable(sessionInfo).map(WxMaJscode2SessionResult::getOpenid).orElse(null);
        } catch (WxErrorException e) {
            // 更具体的异常处理，可以记录日志或者进行其他错误处理逻辑
            log.error("从微信小程序获取会话信息失败, error: {}", e.getError());
            throw new RuntimeException(e);
        }

        //2 根据openid查询数据库表，判断是否第一次登录
        //如果openid不存在返回null，如果存在返回一条记录
        //select * from customer_info ci where ci.wx_open_id = ''
        LambdaQueryWrapper<CustomerInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerInfo::getWxOpenId, openid);
        CustomerInfo customerInfo = customerInfoMapper.selectOne(wrapper);

        //3 如果第一次登录，添加信息到用户表
        if (null == customerInfo) {
             customerInfo = new CustomerInfo();
             customerInfo.setNickname(String.valueOf(System.currentTimeMillis()));
             customerInfo.setAvatarUrl("https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
             customerInfo.setWxOpenId(openid);
            try {
                customerInfoMapper.insert(customerInfo);
            } catch (Exception e) {
                // 处理插入数据库时可能出现的异常
                log.error("Failed to insert new CustomerInfo: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to insert new CustomerInfo", e);
            }
        }

        //4 记录登录日志信息
        CustomerLoginLog customerLoginLog = new CustomerLoginLog();
        customerLoginLog.setCustomerId(customerInfo.getId());
        customerLoginLog.setMsg("小程序登录");
        try {
            customerLoginLogMapper.insert(customerLoginLog);
        } catch (Exception e) {
            // 处理插入数据库时可能出现的异常
            log.error("Failed to insert new customerLoginLog: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to insert new customerLoginLog", e);
        }

        //5 返回用户id
        return customerInfo.getId();
    }

    /**
     * 获取用户登录信息
     * @param customerId
     * @return
     */
    @Override
    public CustomerLoginVo getCustomerInfo(Long customerId) {
        //1 根据用户id查询用户信息
        CustomerInfo customerInfo = customerInfoMapper.selectById(customerId);

        //2 封装到CustomerLoginVo
        CustomerLoginVo customerLoginVo = new CustomerLoginVo();
        //customerLoginVo.setNickname(customerInfo.getNickname());
        BeanUtils.copyProperties(customerInfo,customerLoginVo);

        //@Schema(description = "是否绑定手机号码")
        //    private Boolean isBindPhone;
        String phone = customerInfo.getPhone();
        boolean isBindPhone = StringUtils.hasText(phone);
        customerLoginVo.setIsBindPhone(isBindPhone);

        //3 CustomerLoginVo返回
        return customerLoginVo;
    }

    /**
     * 更新客户微信手机号码实现类
     * @param updateWxPhoneForm
     * @return
     */
    @Override
    public Boolean updateWxPhoneNumber(UpdateWxPhoneForm updateWxPhoneForm) {
        //1 根据Code值获取微信绑定的手机号
        WxMaPhoneNumberInfo phoneNoInfo = null;
        try {
            phoneNoInfo = wxMaService.getUserService().getPhoneNoInfo(updateWxPhoneForm.getCode());
            String phoneNumber = phoneNoInfo.getPhoneNumber();

            //更新用户信息
            Long customerId = updateWxPhoneForm.getCustomerId();
            CustomerInfo customerInfo = customerInfoMapper.selectById(customerId);
            customerInfo.setPhone(phoneNumber);
            customerInfoMapper.updateById(customerInfo);
            return true;
        } catch (WxErrorException e) {
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }

    }

    /**
     * 获取客户OpenId
     * @param customerId
     * @return
     */
    @Override
    public String getCustomerOpenId(Long customerId) {
        LambdaQueryWrapper<CustomerInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerInfo::getId, customerId);
        CustomerInfo customerInfo = customerInfoMapper.selectOne(wrapper);
        return customerInfo.getWxOpenId();
    }
}
