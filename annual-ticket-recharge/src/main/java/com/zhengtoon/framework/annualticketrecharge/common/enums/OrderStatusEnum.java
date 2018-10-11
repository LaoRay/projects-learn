package com.zhengtoon.framework.annualticketrecharge.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 订单状态
 *
 * @author Leiqiyun
 * @date 2018/8/6 9:53
 */
public enum OrderStatusEnum {

    /**
     * 未支付
     */
    O1("未支付", 1),
    /**
     * 已支付
     */
    O2("已支付", 2),
    /**
     * 已失效
     */
    O3("已失效", 3),
    /**
     * 已取消
     */
    O4("已取消", 4),
    /**
     * 订单异常
     */
    O9("订单异常", 9);

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
    private OrderStatusEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }


    public static String getName(int index) {
        for (OrderStatusEnum c : OrderStatusEnum.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public static Integer getIndex(String name) {
        for (OrderStatusEnum c : OrderStatusEnum.values()) {
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

