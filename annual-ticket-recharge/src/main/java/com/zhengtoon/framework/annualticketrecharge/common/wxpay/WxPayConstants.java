package com.zhengtoon.framework.annualticketrecharge.common.wxpay;

/**
 * @author leiqiyun
 */
public interface WxPayConstants {

    String SUCCESS = "SUCCESS";

    String OK = "OK";

    String UTF8 = "UTF8";

    /**
     * 统一下单接口
     */
    String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 查询订单
     */
    String ORDER_QUERY = "https://api.mch.weixin.qq.com/pay/orderquery";

    /**
     * 场景信息
     */
    String SCENE_INFO = "{\"h5_info\": {\"type\":\"WAP\",\"wap_url\": \"www.baidu.com\",\"wap_name\": \"百度一下\"}}";

    /**
     * 订单已支付
     */
    String ORDER_PAID = "ORDERPAID";
}
