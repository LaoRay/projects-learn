package com.zhengtoon.framework.annualticketrecharge.common.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Leiqiyun
 * @date 2018/9/28 10:42
 */
@Data
@ConfigurationProperties(prefix = "wechat")
@Component
public class WechatAccountConfig {
    /**
     * APP ID
     */
    private String appId;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 商户密钥
     */
    private String mchKey;

    /**
     * 商户证书路径
     */
    private String keyPath;

    /**
     * 微信支付异步通知地址
     */
    private String notifyUrl;

    /**
     * 微信支付完成后跳转地址
     */
    private String redirectUrl;
}