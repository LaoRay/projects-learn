package com.zhengtoon.framework.annualticketrecharge.service.impl;

import com.alibaba.fastjson.JSON;
import com.zhengtoon.framework.annualticketrecharge.common.component.WechatAccountConfig;
import com.zhengtoon.framework.annualticketrecharge.common.utils.*;
import com.zhengtoon.framework.annualticketrecharge.common.wxpay.*;
import com.zhengtoon.framework.annualticketrecharge.service.WxPayService;
import com.zhengtoon.framework.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;

/**
 * @author Leiqiyun
 * @date 2018/9/29 10:08
 */
@Service
@Slf4j
public class WxPayServiceImpl implements WxPayService {

    @Autowired
    private WechatAccountConfig wechatAccountConfig;

    @Override
    public WxPaySyncResponse pay(PayRequest request) {
        WxPayUnifiedorderRequest wxRequest = new WxPayUnifiedorderRequest();
        wxRequest.setOutTradeNo(request.getOrderNo());
        wxRequest.setTotalFee(MoneyUtil.yuan2Fen(request.getOrderAmount()));
        wxRequest.setBody(request.getOrderName());

        wxRequest.setTradeType(request.getPayTypeEnum().getType());
        wxRequest.setAppId(wechatAccountConfig.getAppId());
        wxRequest.setMchId(wechatAccountConfig.getMchId());
        wxRequest.setNotifyUrl(wechatAccountConfig.getNotifyUrl());
        wxRequest.setNonceStr(RandomUtils.getRandomStr());
        wxRequest.setSpbillCreateIp(request.getSpbillCreateIp());
        wxRequest.setSceneInfo(WxPayConstants.SCENE_INFO);
        wxRequest.setSign(WxPaySignature.sign(MapUtils.buildMap(wxRequest), wechatAccountConfig.getMchKey()));
        log.info("【微信统一下单】请求参数：wxPayUnifiedorderRequest={}", JSON.toJSONString(wxRequest, true));

        String result = HttpUtils.syncPostString(WxPayConstants.UNIFIED_ORDER_URL, null, XmlUtil.toString(wxRequest));
        log.info("【微信统一下单】响应结果：response={}", result);

        WxPaySyncResponse response = (WxPaySyncResponse) XmlUtil.toObject(result, WxPaySyncResponse.class);
        log.info("【微信统一下单】响应结果：wxPaySyncResponse={}", JSON.toJSONString(response, true));

        if (!WxPayConstants.SUCCESS.equals(response.getReturnCode())) {
            throw new RuntimeException("【微信统一下单】发起支付, returnCode != SUCCESS, returnMsg = " + response.getReturnMsg());
        }

        if (!WxPayConstants.SUCCESS.equals(response.getResultCode())) {
            throw new RuntimeException("【微信统一下单】发起支付, resultCode != SUCCESS, err_code = " + response.getErrCode() + " err_code_des=" + response.getErrCodeDes());
        }
        if (StringUtils.isNotBlank(wechatAccountConfig.getRedirectUrl())) {
            try {
                String encodeUrl = URLEncoder.encode(wechatAccountConfig.getRedirectUrl(), WxPayConstants.UTF8);
                response.setMwebUrl(response.getMwebUrl().concat("&redirect_url=").concat(encodeUrl));
                log.info("【微信统一下单】mwebUrl={}", response.getMwebUrl());
            } catch (Exception e) {
                log.error("redirect url encode error", e);
            }
        }
        return response;
    }

    @Override
    public void asyncNotify(String notifyData) {
        //签名校验
        if (!WxPaySignature.verify(XmlUtil.xmlToMap(notifyData), wechatAccountConfig.getMchKey())) {
            log.error("【微信支付异步通知】签名验证失败, response={}", notifyData);
            throw new RuntimeException("【微信支付异步通知】签名验证失败");
        }

        //xml解析为对象
        WxPayAsyncResponse asyncResponse = (WxPayAsyncResponse) XmlUtil.toObject(notifyData, WxPayAsyncResponse.class);
        log.info("【微信支付异步通知】wxPayAsyncResponse={}", asyncResponse);

        if (!WxPayConstants.SUCCESS.equals(asyncResponse.getReturnCode())) {
            throw new RuntimeException("【微信支付异步通知】发起支付, returnCode != SUCCESS, returnMsg = " + asyncResponse.getReturnMsg());
        }
        //该订单已支付直接返回
        if (!WxPayConstants.SUCCESS.equals(asyncResponse.getResultCode())
                && WxPayConstants.ORDER_PAID.equals(asyncResponse.getErrCode())) {
            log.info("【微信支付异步通知】订单已支付，商户订单号：{} ，微信交易订单号：{}", asyncResponse.getOutTradeNo(), asyncResponse.getTransactionId());
        }

        if (!asyncResponse.getResultCode().equals(WxPayConstants.SUCCESS)) {
            throw new RuntimeException("【微信支付异步通知】发起支付, resultCode != SUCCESS, err_code = " + asyncResponse.getErrCode() + " err_code_des=" + asyncResponse.getErrCodeDes());
        }
        log.info("【微信支付异步通知】订单支付成功，商户订单号：{} ，微信交易订单号：{}", asyncResponse.getOutTradeNo(), asyncResponse.getTransactionId());
    }

    @Override
    public WxQuerySyncResponse queryOrder(QueryRequest queryRequest) {
        WxPayQueryOrderRequest queryOrderRequest = new WxPayQueryOrderRequest();
        queryOrderRequest.setAppId(wechatAccountConfig.getAppId());
        queryOrderRequest.setMchId(wechatAccountConfig.getMchId());
        queryOrderRequest.setTransactionId(queryRequest.getTransactionId());
        queryOrderRequest.setOutTradeNo(queryRequest.getOutTradeNo());
        queryOrderRequest.setNonceStr(RandomUtils.getRandomStr());
        queryOrderRequest.setSign(WxPaySignature.sign(MapUtils.buildMap(queryOrderRequest), wechatAccountConfig.getMchKey()));
        log.info("【微信查询订单】请求参数：wxPayQueryOrderRequest={}", JSON.toJSONString(queryOrderRequest, true));

        String result = HttpUtils.syncPostString(WxPayConstants.ORDER_QUERY, null, XmlUtil.toString(queryOrderRequest));
        log.info("【微信查询订单】响应结果：response={}", result);

        WxQuerySyncResponse syncResponse = (WxQuerySyncResponse) XmlUtil.toObject(result, WxQuerySyncResponse.class);
        log.info("【微信查询订单】wxQuerySyncResponse={}", syncResponse);

        return syncResponse;
    }
}
