package com.zhengtoon.framework.ticketpurchase.entity;

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
@TableName("order_info")
public class OrderInfo extends BaseEntity {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单组号
     */
    @TableField("group_no")
    private Long groupNo;

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
     * 支付平台订单号
     */
    @TableField("platform_order_no")
    private String platformOrderNo;

    /**
     * 支付号
     */
    @TableField("pay_no")
    private String payNo;

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
     * 联系电话
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 支付时间
     */
    @TableField("pay_time")
    private Long payTime;

    /**
     * 门票id
     */
    @TableField("ticket_id")
    private Integer ticketId;

    /**
     * 票名
     */
    @TableField("ticket_name")
    private String ticketName;

    /**
     * 人数
     */
    @TableField("man_num")
    private Integer manNum;

    /**
     * 备注
     */
    @TableField("memo")
    private String memo;
}
