package com.zhengtoon.framework.annualticketrecharge.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.zhengtoon.framework.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author Leiqiyun
 * @date 2018/8/3 11:38
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("pay_record")
public class PayRecord extends BaseEntity {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单号
     */
    @TableField("order_no")
    private Long orderNo;

    /**
     * 外部交易订单号（恩普订单号）
     */
    @TableField("out_trade_no")
    private String outTradeNo;

    /**
     * 微信交易号
     */
    @TableField("transaction_id")
    private String transactionId;

    /**
     * 订单金额
     */
    @TableField("total_fee")
    private BigDecimal totalFee;

    /**
     * 现金支付金额
     */
    @TableField("cash_fee")
    private BigDecimal cashFee;

    /**
     * 订单支付状态 1未支付  2已支付  3支付异常
     */
    @TableField("pay_status")
    private Integer payStatus;

    /**
     * 下单时间
     */
    @TableField("add_time")
    private Long addTime;

    /**
     * 支付时间
     */
    @TableField("pay_time")
    private Long payTime;

    /**
     * 业务类型 1门票购买 2年票充值
     */
    @TableField("business_type")
    private Integer businessType;

    /**
     * 备注（支付异常原因）
     */
    @TableField("remarks")
    private String remarks;
}
