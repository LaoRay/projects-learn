package com.zhengtoon.framework.annualticketrecharge.service;

import com.zhengtoon.framework.annualticketrecharge.common.wxpay.PayRequest;
import com.zhengtoon.framework.annualticketrecharge.common.wxpay.QueryRequest;
import com.zhengtoon.framework.annualticketrecharge.common.wxpay.WxPaySyncResponse;
import com.zhengtoon.framework.annualticketrecharge.common.wxpay.WxQuerySyncResponse;

/**
 * @author leiqiyun
 */
public interface WxPayService {

    /**
     * 发起支付.
     *
     * @param payRequest
     * @return
     */
    WxPaySyncResponse pay(PayRequest payRequest);

    /**
     * 微信支付异步回调
     *
     * @param notifyData
     */
    void asyncNotify(String notifyData);

    /**
     * 微信查询订单
     *
     * @param queryRequest
     * @return
     */
    WxQuerySyncResponse queryOrder(QueryRequest queryRequest);
}
