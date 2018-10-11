package com.zhengtoon.framework.annualticketrecharge.common.wxpay;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 微信查询订单响应
 *
 * @author leiqiyun
 */
@Data
@Root(name = "xml", strict = false)
public class WxQuerySyncResponse {

    @Element(name = "return_code")
    private String returnCode;

    @Element(name = "return_msg", required = false)
    private String returnMsg;

    /**
     * 以下字段在return_code为SUCCESS的时候有返回.
     */
    @Element(name = "appid", required = false)
    private String appid;

    @Element(name = "mch_id", required = false)
    private String mchId;

    @Element(name = "nonce_str", required = false)
    private String nonceStr;

    @Element(name = "sign", required = false)
    private String sign;

    @Element(name = "result_code", required = false)
    private String resultCode;

    @Element(name = "err_code", required = false)
    private String errCode;

    @Element(name = "err_code_des", required = false)
    private String errCodeDes;

    @Element(name = "device_info", required = false)
    private String deviceInfo;

    @Element(name = "openid", required = false)
    private String openid;

    @Element(name = "is_subscribe", required = false)
    private String isSubscribe;

    @Element(name = "trade_type", required = false)
    private String tradeType;

    @Element(name = "trade_state", required = false)
    private String tradeState;

    @Element(name = "bank_type", required = false)
    private String bankType;

    @Element(name = "total_fee", required = false)
    private Integer totalFee;

    @Element(name = "fee_type", required = false)
    private String feeType;

    @Element(name = "cash_fee", required = false)
    private Integer cashFee;

    @Element(name = "cash_fee_type", required = false)
    private String cashFeeType;

    @Element(name = "coupon_fee", required = false)
    private String couponFee;

    @Element(name = "coupon_count", required = false)
    private String couponCount;

    @Element(name = "transaction_id", required = false)
    private String transactionId;

    @Element(name = "out_trade_no", required = false)
    private String outTradeNo;

    @Element(name = "attach", required = false)
    private String attach;

    @Element(name = "time_end", required = false)
    private String timeEnd;

    @Element(name = "trade_state_desc", required = false)
    private String tradeStateDesc;

    @Element(name = "settlement_total_fee", required = false)
    private Integer settlementTotalFee;

    @Element(name = "coupon_type", required = false)
    private String couponType;
}