package com.zhengtoon.framework.ticketpurchase.common.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Leiqiyun
 * @date 2018/8/3 9:53
 */
@Component
@ConfigurationProperties("toonpay")
@Data
public class ToonPayConfig {

    /**
     * 商户号
     */
    private String merNo;

    /**
     * 回调页面
     */
    private String returnUrl;

    /**
     * 回调地址
     */
    private String notifyUrl;

    /**
     * 交易类型
     */
    private String tradeType;

    /**
     * 接入方式
     */
    private String payMethod;

    /**
     * toonType
     */
    private String originApp;

    /**
     * toonPay下单支付接口
     */
    private String createPayOrderH5Url;

    /**
     * 查询订单支付状态接口
     */
    private String tradeOrderDetailUrl;
}
