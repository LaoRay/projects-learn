package com.zhengtoon.framework.annualticketrecharge.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhengtoon.framework.annualticketrecharge.common.component.EnpuConfig;
import com.zhengtoon.framework.annualticketrecharge.common.enums.*;
import com.zhengtoon.framework.annualticketrecharge.common.utils.EnpuRequestUtil;
import com.zhengtoon.framework.annualticketrecharge.common.utils.IMUtil;
import com.zhengtoon.framework.annualticketrecharge.entity.AnnualTicketOrder;
import com.zhengtoon.framework.annualticketrecharge.entity.PayRecord;
import com.zhengtoon.framework.annualticketrecharge.mapper.AnnualTicketOrderMapper;
import com.zhengtoon.framework.annualticketrecharge.mapper.PayRecordMapper;
import com.zhengtoon.framework.exception.CoreExceptionCodes;
import com.zhengtoon.framework.message.im.service.ToonIMService;
import com.zhengtoon.framework.pay.bean.dto.wxpay.WxPayAsyncResponse;
import com.zhengtoon.framework.pay.bean.dto.wxpay.WxPaySyncResponse;
import com.zhengtoon.framework.pay.constants.WxPayConstants;
import com.zhengtoon.framework.pay.process.PayProcess;
import com.zhengtoon.framework.pay.utils.MoneyUtil;
import com.zhengtoon.framework.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Leiqiyun
 * @date 2018/10/8 10:03
 */
@Service
@Slf4j
public class AnnualTicketPayProcess extends PayProcess {

    @Autowired
    private AnnualTicketOrderMapper annualTicketOrderMapper;
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

        AnnualTicketOrder entity = new AnnualTicketOrder();
        entity.setEpOrderNo(asyncResponse.getOutTradeNo());
        log.debug("【微信异步回调】annualTicketOrderMapper={}", annualTicketOrderMapper);
        AnnualTicketOrder annualTicketOrder = annualTicketOrderMapper.selectOne(entity);
        log.debug("【微信异步回调】OrderInfo={}", JSON.toJSONString(annualTicketOrder, true));
        annualTicketOrder.setPayNo(asyncResponse.getTransactionId());
        annualTicketOrder.setPayTime(DateUtil.parseDateLongFormat(asyncResponse.getTimeEnd()).getTime());

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
            enpuPay(annualTicketOrder, payRecord);
        } else {
            handleFailOrder(annualTicketOrder, payRecord, asyncResponse.getErrCodeDes());
        }

        payRecord.setOrderNo(annualTicketOrder.getOrderNo());
        payRecord.setOutTradeNo(asyncResponse.getOutTradeNo());
        payRecord.setTransactionId(asyncResponse.getTransactionId());
        payRecord.setTotalFee(MoneyUtil.fen2Yuan(asyncResponse.getTotalFee()));
        payRecord.setCashFee(MoneyUtil.fen2Yuan(asyncResponse.getCashFee()));
        payRecord.setAddTime(annualTicketOrder.getAddTime());
        payRecord.setPayTime(System.currentTimeMillis());
        payRecord.setBusinessType(BusinessTypeEnum.B2.getIndex());
        log.debug("【微信异步回调】支付流水：PayRecord={}", payRecord);
        payRecordMapper.insert(payRecord);
    }

    private void enpuPay(AnnualTicketOrder annualTicketOrder, PayRecord payRecord) {
        JSONObject body = new JSONObject();
        body.put("orderno", annualTicketOrder.getEpOrderNo());
        body.put("amount", annualTicketOrder.getAmount());
        body.put("payno", annualTicketOrder.getPayNo());
        JSONObject resultObject;
        Integer code = null;
        String msg = null;
        try {
            String resultString = EnpuRequestUtil.requestEnpu(EnpuInterfaceEnum.NP_ORDER_PAY.getValue(), body.toJSONString(), enpuConfig);
            log.info("【恩普票务】支付响应：{}", resultString);
            resultObject = JSONObject.parseObject(resultString);
            code = resultObject.getInteger(EnpuRequestUtil.CODE);
            msg = resultObject.getString("msg");
        } catch (Exception e) {
            log.info("【恩普票务】支付异常，恩普订单号：{}，原因：{}", annualTicketOrder.getEpOrderNo(), e);
            handleFailOrder(annualTicketOrder, payRecord, e.getMessage());
        }
        if (CoreExceptionCodes.SUCCESS.getCode().equals(code)) {
            log.info("【恩普票务】支付成功，恩普订单号：{}", annualTicketOrder.getEpOrderNo());
            handleSuccessOrder(annualTicketOrder, payRecord);
        } else if (EnpuRequestUtil.ORDER_PAID.equals(code)) {
            log.info("【恩普票务】订单已支付，无需再次支付，恩普订单号：{}", annualTicketOrder.getEpOrderNo());
            handleSuccessOrder(annualTicketOrder, payRecord);
        } else {
            log.info("【恩普票务】支付异常，恩普订单号：{}，原因：{}", annualTicketOrder.getEpOrderNo(), msg);
            handleFailOrder(annualTicketOrder, payRecord, msg);
        }
    }

    private void handleSuccessOrder(AnnualTicketOrder annualTicketOrder, PayRecord payRecord) {
        payRecord.setPayStatus(PayStatusEnum.P2.getIndex());

        annualTicketOrder.setFlag(OrderStatusEnum.O2.getIndex());
        annualTicketOrderMapper.updateById(annualTicketOrder);
        // 订单支付成功消息推送
        IMUtil.notice(toonIMService, annualTicketOrder.getUserId(), IMUtil.RECHARGE_SUCCESS, IMTypeEnum.ANNUAL_TICKET_RECHARGE.getName(),
                annualTicketOrder.getEpOrderNo(), annualTicketOrder.getAmount(), annualTicketOrder.getValidTerm());
    }

    private void handleFailOrder(AnnualTicketOrder annualTicketOrder, PayRecord payRecord, String errorMsg) {
        payRecord.setPayStatus(PayStatusEnum.P3.getIndex());
        payRecord.setRemarks(errorMsg);

        annualTicketOrder.setFlag(OrderStatusEnum.O9.getIndex());
        annualTicketOrderMapper.updateById(annualTicketOrder);
        // 订单支付异常消息推送
        IMUtil.notice(toonIMService, annualTicketOrder.getUserId(), IMUtil.RECHARGE_FAIL, IMTypeEnum.ANNUAL_TICKET_RECHARGE.getName(),
                annualTicketOrder.getEpOrderNo(), annualTicketOrder.getAmount());
    }
}
