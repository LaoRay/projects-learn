package com.zhengtoon.framework.annualticketrecharge.common.enums;

/**
 * @author leiqiyun
 */
public enum PayTypeEnum {

    /**
     * 微信H5支付
     */
    WXPAY_MWEB("MWEB", "微信H5支付");

    private String type;

    private String name;

    PayTypeEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
