package com.zhengtoon.framework.ticketpurchase.common.exception;


import com.zhengtoon.framework.entity.CodeMessage;

/**
 * 应用异常编码
 */
public class ExceptionCode {

    /**
     * 服务器异常
     */
    public static final CodeMessage SERVICE_BUSY = new CodeMessage(999999, "服务器繁忙，请稍后重试");

    /**
     * 订单状态错误
     */
    public static final CodeMessage ORDER_STATUS_ERROR = new CodeMessage(800001, "订单状态错误");

    /**
     * 订单不存在
     */
    public static final CodeMessage ORDER_NOT_EXIST = new CodeMessage(800002, "订单不存在");

    /**
     * 支付金额与订单金额不符
     */
    public static final CodeMessage ORDER_AMOUNT_MISMATCH = new CodeMessage(800003, "支付金额与订单金额不符");
    
    /**
     * 超过游览日期当天16点不予退票
     */
    public static final CodeMessage OVER_USE_TIME = new CodeMessage(800004, "超过游览日期当天16点不予退票");
}