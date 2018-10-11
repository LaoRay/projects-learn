package com.zhengtoon.framework.ticketpurchase.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Leiqiyun
 * @date 2018/8/15 15:50
 */
@Data
public class ToonPayResultVO {

    /**
     * 聚合支付单号
     */
    private String tradeFlowNo;

    /**
     * 交易订单号（恩普订单号）
     */
    private String outTradeNo;

    /**
     * 交易状态
     */
    private Integer tradeStatus;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 支付渠道编码
     */
    private String channelCode;

    /**
     * 商户号
     */
    private String sellerId;

    /**
     * 商家
     */
    private String sellerName;

    /**
     * 交易标题
     */
    private String tradeTitle;

    /**
     * 交易主题
     */
    private String tradeSubject;

    /**
     * 下单支付时间
     */
    private String createdDate;

    /**
     * 订单组号
     */
    private Long groupNo;

    /**
     * 门票id
     */
    private Integer ticketId;
}
