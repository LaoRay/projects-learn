package com.zhengtoon.framework.ticketpurchase.common.enums;

/**
 * 消息通知类别
 *
 * @author Leiqiyun
 * @date 2018/8/10 9:53
 */
public enum IMTypeEnum {

    /**
     * 购票信息
     */
    TICKET_PURCHASE("购票信息"),
    /**
     * 改签
     */
    TICKET_CHANGE("改签"),
    /**
     * 退票
     */
    TICKET_REFUND("退票"),
    /**
     * 游览提醒
     */
    VISIT_REMIND("游览提醒");

    private String name;

    IMTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
