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
 * @date 2018/8/6 10:20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("order_ticket")
public class OrderTicket extends BaseEntity {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 票号
     */
    @TableField("tid")
    private String tid;

    /**
     * 订单号
     */
    @TableField("order_no")
    private String orderNo;

    /**
     * 恩普票务订单号
     */
    @TableField("ep_order_no")
    private String epOrderNo;

    /**
     * 票名
     */
    @TableField("ticket_name")
    private String ticketName;

    /**
     * 票类型
     */
    @TableField("ticket_type")
    private Integer ticketType;

    /**
     * 简介
     */
    @TableField("summary")
    private String summary;

    /**
     * 票面价格
     */
    @TableField("single_price")
    private BigDecimal singlePrice;

    /**
     * 销售价格
     */
    @TableField("tprice")
    private BigDecimal tprice;

    /**
     * 人数
     */
    @TableField("man_num")
    private Integer manNum;

    /**
     * 使用时间
     */
    @TableField("use_time")
    private String useTime;

    /**
     * 二维码图片地址
     */
    @TableField("qr")
    private String qr;

    /**
     * 二维码图片数据流
     */
    @TableField("qr_stream")
    private String qrStream;

    /**
     * 票状态  1未支付 2已支付 3已退票 4已使用 5已取票不允许退 6已取票可退 7已改签 8已失效
     */
    @TableField("flag")
    private Integer flag;

    /**
     * 退票时间
     */
    @TableField("refund_time")
    private Long refundTime;

    /**
     * 改签时间
     */
    @TableField("change_time")
    private Long changeTime;

    /**
     * 是否套票 1是套票 0是单票
     */
    @TableField("if_taopiao")
    private Integer ifTaopiao;

    /**
     * 兑换码  空值表示为电子票；非空表示为兑换码票， 需要凭兑换码换票
     */
    @TableField("exchange_code")
    private String exchangeCode;
}
