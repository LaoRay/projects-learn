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
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("annual_ticket_order")
public class AnnualTicketOrder extends BaseEntity {

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
     * 恩普票务订单号
     */
    @TableField("ep_order_no")
    private String epOrderNo;

    /**
     * 支付平台订单号
     */
    @TableField("platform_order_no")
    private String platformOrderNo;

    /**
     * 支付单号
     */
    @TableField("pay_no")
    private String payNo;

    /**
     * 支付时间
     */
    @TableField("pay_time")
    private Long payTime;

    /**
     * 是否调用过聚合支付 1是 0否
     */
    @TableField("pay_flag")
    private Integer payFlag;

    /**
     * 订单金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 下单时间
     */
    @TableField("add_time")
    private Long addTime;

    /**
     * 订单状态 1未支付  2已支付  3已失效  4已取消
     */
    @TableField("flag")
    private Integer flag;

    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 取票人
     */
    @TableField("cn_name")
    private String cnName;

    /**
     * 身份证号
     */
    @TableField("id_card")
    private String idCard;

    /**
     * 联系电话
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 年卡名称
     */
    @TableField("card_name")
    private String cardName;

    /**
     * 年卡类型
     */
    @TableField("card_type")
    private String cardType;

    /**
     * 有效期
     */
    @TableField("valid_term")
    private String validTerm;
}
