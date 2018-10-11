package com.zhengtoon.framework.ticketpurchase.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 票状态
 *
 * @author Leiqiyun
 * @date 2018/8/6 9:53
 */
public enum TicketStatusEnum {

    /**
     * 未支付
     */
    T1("未支付", 1),
    /**
     * 已支付
     */
    T2("已支付", 2),
    /**
     * 已退票
     */
    T3("已退票", 3),
    /**
     * 已使用
     */
    T4("已使用", 4),
    /**
     * 已取票不允许退
     */
    T5("已取票不允许退", 5),
    /**
     * 已取票可退
     */
    T6("已取票可退", 6),
    /**
     * 已改签
     */
    T7("已改签", 7),
    /**
     * 已过期
     */
    T8("已过期", 8);

    /**
     * 成员变量
     */
    private String name;
    private int index;

    /**
     * 构造方法
     *
     * @param name
     * @param index
     */
    private TicketStatusEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }


    public static String getName(int index) {
        for (TicketStatusEnum c : TicketStatusEnum.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public static int getIndex(String name) {
        for (TicketStatusEnum c : TicketStatusEnum.values()) {
            if (StringUtils.equals(c.name, name)) {
                return c.index;
            }
        }
        return T1.getIndex();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return this.index + ":代表" + this.name;
    }
}

