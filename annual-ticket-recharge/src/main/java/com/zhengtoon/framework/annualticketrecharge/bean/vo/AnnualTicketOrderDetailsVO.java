package com.zhengtoon.framework.annualticketrecharge.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Leiqiyun
 * @date 2018/9/12 11:38
 */
@Data
public class AnnualTicketOrderDetailsVO {

    /**
     * 订单号
     */
    private Long orderNo;

    /**
     * 恩普票务订单号
     */
    private String epOrderNo;

    /**
     * 支付单号
     */
    private String payNo;

    /**
     * 支付时间
     */
    private Long payTime;

    /**
     * 是否调用过聚合支付 1是 0否
     */
    private Integer payFlag;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 下单时间
     */
    private Long addTime;

    /**
     * 订单状态 1未支付  2已支付  3已失效  4已取消
     */
    private Integer flag;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 取票人
     */
    private String cnName;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 年卡名称
     */
    private String cardName;

    /**
     * 年卡类型
     */
    private String cardType;

    /**
     * 有效期
     */
    private String validTerm;
}
