package com.zhengtoon.framework.annualticketrecharge.common.exception;


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
     * 登陆异常
     */
    public static final CodeMessage LOGIN_ERROR = new CodeMessage(201, "账号不存在");
    public static final CodeMessage PASSWORD_ERROR = new CodeMessage(202, "密码错误");
    public static final CodeMessage SECURITYCODE_ERROR = new CodeMessage(203, "验证码错误");
    public static final CodeMessage SECURITYCODE_OVERTIME = new CodeMessage(204, "验证码超时");
    public static final CodeMessage SECURITYCODE_NONENTITY = new CodeMessage(205, "验证码不存在");
    public static final CodeMessage LOGIN_FAIL = new CodeMessage(206, "用户未登录，请重新登陆");
    public static final CodeMessage COMPARE_PASSWORD = new CodeMessage(207, "两次输入的密码不一致");
    public static final CodeMessage UPDATE_PASSWORD = new CodeMessage(208, "修改密码失败");
    public static final CodeMessage REGISTER_ERROR = new CodeMessage(209, "注册失败");
}