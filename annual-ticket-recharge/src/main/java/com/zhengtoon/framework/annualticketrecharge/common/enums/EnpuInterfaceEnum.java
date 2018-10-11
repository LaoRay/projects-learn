package com.zhengtoon.framework.annualticketrecharge.common.enums;

/**
 * 恩普接口名
 *
 * @author Leiqiyun
 * @date 2018/8/3 9:53
 */
public enum EnpuInterfaceEnum {

    /**
     * 添加年票充值订单
     */
    ADD_NP_ORDER("NPadd", "添加年票充值订单"),

    /**
     * 取消订单
     */
    CANCEL_ORDER("cancel", "取消订单"),

    /**
     * 年票充值订单支付
     */
    NP_ORDER_PAY("NPpay", "年票充值订单支付"),

    /**
     * 查询年票订单明细
     */
    NP_ORDER_INFO("NPorderInfo", "查询年票订单明细");

    private String value;

    private String description;

    EnpuInterfaceEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
