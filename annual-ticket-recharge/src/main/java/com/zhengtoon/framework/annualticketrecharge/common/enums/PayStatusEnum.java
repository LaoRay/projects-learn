package com.zhengtoon.framework.annualticketrecharge.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 支付状态
 *
 * @author Leiqiyun
 * @date 2018/8/6 9:53
 */
public enum PayStatusEnum {

    /**
     * 待支付
     */
    P1("待支付", 1),
    /**
     * 已支付
     */
    P2("已支付", 2),
    /**
     * 支付异常
     */
    P3("支付异常", 3);

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
    private PayStatusEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }


    public static String getName(int index) {
        for (PayStatusEnum c : PayStatusEnum.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public static Integer getIndex(String name) {
        for (PayStatusEnum c : PayStatusEnum.values()) {
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

