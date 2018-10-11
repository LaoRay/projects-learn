package com.zhengtoon.framework.annualticketrecharge.common.wxpay;

import com.zhengtoon.framework.annualticketrecharge.common.enums.PayTypeEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Leiqiyun
 * @date 2018/9/29 9:59
 */
@Data
public class PayRequest {
    /**
     * 支付方式.
     */
    private PayTypeEnum payTypeEnum;

    /**
     * 订单号.
     */
    private String orderNo;

    /**
     * 订单金额.
     */
    private BigDecimal orderAmount;

    /**
     * 订单名字.
     */
    private String orderName;

    /**
     * 客户端访问Ip  外部H5支付时必传，需要真实Ip
     */
    private String spbillCreateIp;
}
