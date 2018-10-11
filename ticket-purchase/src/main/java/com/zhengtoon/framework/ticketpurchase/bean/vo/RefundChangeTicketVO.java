package com.zhengtoon.framework.ticketpurchase.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 可退改签的门票
 *
 * @author Leiqiyun
 * @date 2018/8/15 17:27
 */
@Data
public class RefundChangeTicketVO {
    /**
     * 系统订单号
     */
    private String orderNo;

    /**
     * 恩普订单号
     */
    private String epOrderNo;

    /**
     * 票号
     */
    private String tid;

    /**
     * 票名
     */
    private String ticketName;

    /**
     * 票状态
     */
    private Integer ticketFlag;

    /**
     * 人数
     */
    private Integer manNum;

    /**
     * 订单状态
     */
    private Integer flag;

    /**
     * 下单时间
     */
    private Long addTime;

    /**
     * 订单金额
     */
    private BigDecimal amount;
}
