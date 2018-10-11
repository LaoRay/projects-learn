package com.zhengtoon.framework.ticketpurchase.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhengtoon.framework.exception.CoreExceptionCodes;
import com.zhengtoon.framework.message.im.service.ToonIMService;
import com.zhengtoon.framework.pay.bean.dto.wxpay.WxPayAsyncResponse;
import com.zhengtoon.framework.pay.bean.dto.wxpay.WxPaySyncResponse;
import com.zhengtoon.framework.pay.constants.WxPayConstants;
import com.zhengtoon.framework.pay.process.PayProcess;
import com.zhengtoon.framework.pay.utils.MoneyUtil;
import com.zhengtoon.framework.ticketpurchase.common.component.EnpuConfig;
import com.zhengtoon.framework.ticketpurchase.common.enums.*;
import com.zhengtoon.framework.ticketpurchase.common.utils.EnpuRequestUtil;
import com.zhengtoon.framework.ticketpurchase.common.utils.IMUtil;
import com.zhengtoon.framework.ticketpurchase.entity.OrderInfo;
import com.zhengtoon.framework.ticketpurchase.entity.PayRecord;
import com.zhengtoon.framework.ticketpurchase.mapper.OrderInfoMapper;
import com.zhengtoon.framework.ticketpurchase.mapper.PayRecordMapper;
import com.zhengtoon.framework.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Leiqiyun
 * @date 2018/10/8 14:48
 */
@Service
@Slf4j
public class TicketPayProcess extends PayProcess {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private PayRecordMapper payRecordMapper;

    @Autowired
    private ToonIMService toonIMService;

    @Autowired
    private EnpuConfig enpuConfig;

    @Override
    public void paySuccessProcess(WxPaySyncResponse response) {

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void notifyPaySuccessProcess(WxPayAsyncResponse asyncResponse) {
        log.debug("【微信异步回调】WxPayAsyncResponse={}", JSON.toJSONString(asyncResponse, true));
        if (WxPayConstants.ORDER_PAID.equals(asyncResponse.getErrCode())) {
            log.debug("【微信异步回调】订单已支付，商户订单号：{}，微信交易号：{}", asyncResponse.getOutTradeNo(), asyncResponse.getTransactionId());
            return;
        }

        OrderInfo entity = new OrderInfo();
        entity.setEpOrderNo(asyncResponse.getOutTradeNo());
        log.debug("【微信异步回调】orderInfoMapper={}", orderInfoMapper);
        OrderInfo orderInfo = orderInfoMapper.selectOne(entity);
        log.debug("【微信异步回调】OrderInfo={}", JSON.toJSONString(orderInfo, true));
        orderInfo.setPayNo(asyncResponse.getTransactionId());
        orderInfo.setPayTime(DateUtil.parseDateLongFormat(asyncResponse.getTimeEnd()).getTime());

        // 支付流水
        PayRecord record = new PayRecord();
        record.setOutTradeNo(asyncResponse.getOutTradeNo());
        PayRecord payRecord = payRecordMapper.selectOne(record);
        if (payRecord == null) {
            payRecord = new PayRecord();
        }
        log.debug("【微信异步回调】PayRecord={}", payRecord);
        if (WxPayConstants.SUCCESS.equals(asyncResponse.getResultCode())) {
            log.debug("【微信异步回调】调用恩普支付");
            enpuPay(orderInfo, payRecord);
        } else {
            handleFailOrder(orderInfo, payRecord, asyncResponse.getErrCodeDes());
        }

        payRecord.setOrderNo(Long.parseLong(orderInfo.getOrderNo()));
        payRecord.setOutTradeNo(asyncResponse.getOutTradeNo());
        payRecord.setTransactionId(asyncResponse.getTransactionId());
        payRecord.setTotalFee(MoneyUtil.fen2Yuan(asyncResponse.getTotalFee()));
        payRecord.setCashFee(MoneyUtil.fen2Yuan(asyncResponse.getCashFee()));
        payRecord.setAddTime(orderInfo.getAddTime());
        payRecord.setPayTime(System.currentTimeMillis());
        payRecord.setBusinessType(BusinessTypeEnum.B1.getIndex());
        log.debug("【微信异步回调】支付流水：PayRecord={}", payRecord);
        payRecordMapper.insert(payRecord);
    }

    private void enpuPay(OrderInfo orderInfo, PayRecord payRecord) {
        JSONObject body = new JSONObject();
        body.put("orderno", orderInfo.getEpOrderNo());
        body.put("amount", orderInfo.getAmount());
        body.put("payno", orderInfo.getPayNo());
        JSONObject resultObject;
        Integer code = null;
        String msg = null;
        try {
            String resultString = EnpuRequestUtil.requestEnpu(EnpuInterfaceEnum.ORDER_PAY.getValue(), body.toJSONString(), enpuConfig);
            log.info("【恩普票务】支付响应：{}", resultString);
            resultObject = JSONObject.parseObject(resultString);
            code = resultObject.getInteger(EnpuRequestUtil.CODE);
            msg = resultObject.getString("msg");
        } catch (Exception e) {
            log.info("【恩普票务】支付异常，恩普订单号：{}，原因：{}", orderInfo.getEpOrderNo(), e);
            handleFailOrder(orderInfo, payRecord, e.getMessage());
            return;
        }
        if (CoreExceptionCodes.SUCCESS.getCode().equals(code)) {
            log.info("【恩普票务】支付成功，恩普订单号：{}", orderInfo.getEpOrderNo());
            handleSuccessOrder(orderInfo, payRecord);
        } else if (EnpuRequestUtil.ORDER_PAID.equals(code)) {
            log.info("【恩普票务】订单已支付，无需再次支付，恩普订单号：{}", orderInfo.getEpOrderNo());
            handleSuccessOrder(orderInfo, payRecord);
        } else {
            log.info("【恩普票务】支付异常，恩普订单号：{}，原因：{}", orderInfo.getEpOrderNo(), msg);
            handleFailOrder(orderInfo, payRecord, msg);
        }
    }

    private void handleSuccessOrder(OrderInfo orderInfo, PayRecord payRecord) {
        payRecord.setPayStatus(PayStatusEnum.P2.getIndex());

        orderInfo.setFlag(OrderStatusEnum.O2.getIndex());
        orderInfoMapper.updateById(orderInfo);
        // 订单支付成功消息推送
        IMUtil.notice(toonIMService, orderInfo.getUserId(), IMUtil.TICKET_PAY_SUCCESS, IMTypeEnum.TICKET_PURCHASE.getName(),
                orderInfo.getEpOrderNo(), orderInfo.getAmount());
    }

    private void handleFailOrder(OrderInfo orderInfo, PayRecord payRecord, String errorMsg) {
        payRecord.setPayStatus(PayStatusEnum.P3.getIndex());
        payRecord.setRemarks(errorMsg);

        orderInfo.setFlag(OrderStatusEnum.O9.getIndex());
        orderInfoMapper.updateById(orderInfo);
        // 订单支付异常消息推送
        IMUtil.notice(toonIMService, orderInfo.getUserId(), IMUtil.TICKET_PAY_FAIL, IMTypeEnum.TICKET_PURCHASE.getName(),
                orderInfo.getEpOrderNo(), orderInfo.getAmount());
    }
}
