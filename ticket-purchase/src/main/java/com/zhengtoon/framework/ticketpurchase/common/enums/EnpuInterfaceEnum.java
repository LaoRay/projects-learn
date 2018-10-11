package com.zhengtoon.framework.ticketpurchase.common.enums;

/**
 * 恩普接口名
 *
 * @author Leiqiyun
 * @date 2018/8/3 9:53
 */
public enum EnpuInterfaceEnum {

    /**
     * 获取票类信息接口
     */
    GET_PRODUCT("GetProduct", "获取票类信息"),

    /**
     * 添加二维码订单
     */
    ADD_ORDER("addqr", "添加二维码订单"),

    /**
     * 取消订单
     */
    CANCEL_ORDER("cancel", "取消订单"),

    /**
     * 根据订单号查询门票列表
     */
    SEARCH_ORDER_DETAILS("searchByOrderno", "订单详情"),

    /**
     * 根据票号查询门票凭证
     */
    SEARCH_TICKET("searchTicket", "门票凭证"),

    /**
     * 门票改签
     */
    TICKET_CHANGE("change", "门票改签"),

    /**
     * 退票
     */
    TICKET_REFUND("refund", "退票"),

    /**
     * 订单支付
     */
    ORDER_PAY("pay", "订单支付");

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
