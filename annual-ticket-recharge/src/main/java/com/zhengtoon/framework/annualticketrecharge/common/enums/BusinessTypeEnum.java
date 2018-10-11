package com.zhengtoon.framework.annualticketrecharge.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 业务类型
 *
 * @author Leiqiyun
 * @date 2018/8/6 9:53
 */
public enum BusinessTypeEnum {

    /**
     * 门票购买
     */
    B1("门票购买", 1),
    /**
     * 年票充值
     */
    B2("年票充值", 2);

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
    private BusinessTypeEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }


    public static String getName(int index) {
        for (BusinessTypeEnum c : BusinessTypeEnum.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public static Integer getIndex(String name) {
        for (BusinessTypeEnum c : BusinessTypeEnum.values()) {
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

