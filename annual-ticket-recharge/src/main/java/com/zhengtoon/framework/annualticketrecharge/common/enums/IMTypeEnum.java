package com.zhengtoon.framework.annualticketrecharge.common.enums;

/**
 * 消息通知类别
 *
 * @author Leiqiyun
 * @date 2018/8/10 9:53
 */
public enum IMTypeEnum {

    /**
     * 充值信息
     */
    ANNUAL_TICKET_RECHARGE("充值信息");

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
