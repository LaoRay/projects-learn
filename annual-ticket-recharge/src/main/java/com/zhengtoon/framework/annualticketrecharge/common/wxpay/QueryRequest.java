package com.zhengtoon.framework.annualticketrecharge.common.wxpay;

import lombok.Data;

/**
 * @author Leiqiyun
 * @date 2018/9/30 10:59
 */
@Data
public class QueryRequest {

    /**
     * 微信的订单号
     */
    private String transactionId;

    /**
     * 商户系统内部订单号
     */
    private String outTradeNo;
}
