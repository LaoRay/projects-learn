package com.zhengtoon.framework.ticketpurchase.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 票类型
 *
 * @author Leiqiyun
 * @date 2018/8/6 9:53
 */
public enum TicketTypeEnum {

    /**
     * 全价票
     */
    TT1("全价票", 1),
    /**
     * 半价票
     */
    TT2("半价票", 2),
    /**
     * 三联票
     */
    TT3("三联票", 3),
    /**
     * 四联票
     */
    TT4("四联票", 4);

    /**
     * 成员变量
     */
    private String name;
    private Integer index;

    /**
     * 构造方法
     *
     * @param name
     * @param index
     */
    private TicketTypeEnum(String name, Integer index) {
        this.name = name;
        this.index = index;
    }


    public static String getName(Integer index) {
        if (index == null) {
            return TicketTypeEnum.TT1.name;
        }
        for (TicketTypeEnum c : TicketTypeEnum.values()) {
            if (c.getIndex().equals(index)) {
                return c.name;
            }
        }
        return null;
    }

    public static Integer getIndex(String name) {
        for (TicketTypeEnum c : TicketTypeEnum.values()) {
            if (StringUtils.equals(c.name, name)) {
                return c.index;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return this.index + ":代表" + this.name;
    }
}
