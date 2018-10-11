package com.zhengtoon.framework.annualticketrecharge.bean.dto;

import lombok.Data;

/**
 * @author Leiqiyun
 * @date 2018/9/12 11:41
 */
@Data
public class OrderNoDTO {

    /**
     * 系统订单号
     */
    private Long orderNo;

    /**
     * 恩普订单号
     */
    private String epOrderNo;
}
