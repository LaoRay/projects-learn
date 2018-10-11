package com.zhengtoon.framework.ticketpurchase.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.zhengtoon.framework.entity.ResponseResult;
import com.zhengtoon.framework.exception.BusinessException;
import com.zhengtoon.framework.exception.CoreExceptionCodes;
import com.zhengtoon.framework.jwt.bean.dto.UserInfo;
import com.zhengtoon.framework.jwt.common.SessionUtils;
import com.zhengtoon.framework.message.im.service.ToonIMService;
import com.zhengtoon.framework.ticketpurchase.bean.dto.OrderInfoDTO;
import com.zhengtoon.framework.ticketpurchase.bean.dto.OrderNoDTO;
import com.zhengtoon.framework.ticketpurchase.bean.dto.OrderSaveDTO;
import com.zhengtoon.framework.ticketpurchase.bean.dto.ToonPayCallBackDTO;
import com.zhengtoon.framework.ticketpurchase.bean.vo.*;
import com.zhengtoon.framework.ticketpurchase.common.component.EnpuConfig;
import com.zhengtoon.framework.ticketpurchase.common.component.ToonPayConfig;
import com.zhengtoon.framework.ticketpurchase.common.enums.EnpuInterfaceEnum;
import com.zhengtoon.framework.ticketpurchase.common.enums.IMTypeEnum;
import com.zhengtoon.framework.ticketpurchase.common.enums.OrderStatusEnum;
import com.zhengtoon.framework.ticketpurchase.common.enums.TicketStatusEnum;
import com.zhengtoon.framework.ticketpurchase.common.exception.ExceptionCode;
import com.zhengtoon.framework.ticketpurchase.common.utils.EnpuRequestUtil;
import com.zhengtoon.framework.ticketpurchase.common.utils.IMUtil;
import com.zhengtoon.framework.ticketpurchase.entity.OrderInfo;
import com.zhengtoon.framework.ticketpurchase.entity.OrderTicket;
import com.zhengtoon.framework.ticketpurchase.mapper.OrderInfoMapper;
import com.zhengtoon.framework.ticketpurchase.service.IdGeneratorService;
import com.zhengtoon.framework.ticketpurchase.service.OrderService;
import com.zhengtoon.framework.ticketpurchase.service.TicketService;
import com.zhengtoon.framework.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Leiqiyun
 * @date 2018/8/3 11:17
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderService {

    @Autowired
    private EnpuConfig enpuConfig;
    @Autowired
    private ToonPayConfig toonPayConfig;
    @Autowired
    private TicketService ticketService;
    @Autowired
    @Qualifier("redisIdGeneratorService")
    private IdGeneratorService redisIdGeneratorService;
    @Autowired
    private ToonIMService toonIMService;
    @Autowired
    private RedisTemplate redisTemplate;

    private static final String SUCCESS = "success";
    private static final String ERROR = "error";
    private static final Integer IS_PAID = 1;

    @Override
    public OrderSaveVO saveOrder(OrderSaveDTO orderSaveDTO) {
        UserInfo userInfo = SessionUtils.getUserInfo();
        OrderInfoDTO orderInfo = orderSaveDTO.getOrderinfo();
        if (StringUtils.isNotBlank(orderInfo.getOrderno())) {
            deleteOrder(orderInfo.getOrderno());
        }
        Long groupNo = orderInfo.getGroupNo();
        if (groupNo == null) {
            groupNo = redisIdGeneratorService.generatorId();
        }
        String orderNo = redisIdGeneratorService.generatorId() + "";
        orderInfo.setPartnerOrderno(orderNo);
        orderInfo.setCnname(userInfo.getCertName());
        orderInfo.setIdcard(userInfo.getCertNo());
        if (StringUtils.isBlank(orderInfo.getMobile())) {
            orderInfo.setMobile(userInfo.getMobile());
        }
        String resultString = EnpuRequestUtil.requestEnpu(EnpuInterfaceEnum.ADD_ORDER.getValue(), JSON.toJSONString(orderSaveDTO), enpuConfig);
        if (StringUtils.isNotBlank(resultString)) {
            JSONObject resultObject = JSONObject.parseObject(resultString);
            if (CoreExceptionCodes.SUCCESS.getCode().equals(resultObject.getInteger(EnpuRequestUtil.CODE))) {
                OrderSaveVO orderSaveVO = JSONObject.parseObject(new String(Base64Utils.decodeFromString(resultObject.getString("body"))), OrderSaveVO.class);
                log.info("【恩普票务】下单响应body：{}", orderSaveVO);
                orderSaveVO.setPartnerOrderno(orderNo);
                orderSaveVO.setGroupNo(groupNo);
                OrderInfo order = assembleOrderInfo(orderSaveVO, orderSaveDTO, userInfo);
                super.insert(order);
                // 下单成功后，同步恩普门票到本地数据库
                ticketService.listTicketByOrderNo(orderSaveVO.getOrderno());
                return orderSaveVO;
            }
            log.error("下单失败，原因：{}", resultObject.getString("msg"));
            throw new BusinessException(resultObject.getInteger("code"), resultObject.getString("msg"));
        }
        return new OrderSaveVO();
    }

    private OrderInfo assembleOrderInfo(OrderSaveVO orderSaveVO, OrderSaveDTO orderSaveDTO, UserInfo userInfo) {
        OrderInfo order = new OrderInfo();
        order.setGroupNo(orderSaveVO.getGroupNo());
        order.setOrderNo(orderSaveVO.getPartnerOrderno());
        order.setEpOrderNo(orderSaveVO.getOrderno());
        order.setAmount(orderSaveVO.getAmount());
        order.setAddTime(System.currentTimeMillis());
        order.setFlag(TicketStatusEnum.T1.getIndex());
        order.setUserId(userInfo.getUserId());
        order.setCnName(userInfo.getCertName());
        order.setMobile(orderSaveDTO.getOrderinfo().getMobile());
        order.setTicketId(orderSaveDTO.getTicket().get(0).getTicketid());
        order.setTicketName(orderSaveDTO.getTicket().get(0).getTicketName());
        order.setManNum(orderSaveDTO.getTicket().get(0).getNumber());
        order.setMemo(orderSaveDTO.getOrderinfo().getMemo());
        return order;
    }

    @Override
    public OrderDetailsVO findOrderDetails(Long groupNo) {
        OrderInfo orderInfo = findOrderByGroupNo(groupNo);
        if (orderInfo != null) {
            List<OrderTicket> ticketList = ticketService.listTicketByOrderNo(orderInfo.getEpOrderNo());
            return OrderDetailsVO.builder()
                    .orderInfo(orderInfo)
                    .ticketList(ticketList)
                    .build();
        }
        log.error("订单详情查询失败，原因：订单不存在，订单组号：{}", groupNo);
        throw new BusinessException(ExceptionCode.ORDER_NOT_EXIST);
    }

    private OrderInfo findOrderByGroupNo(Long groupNo) {
        return super.selectOne(Condition.create().eq("group_no", groupNo));
    }

    @Override
    public Boolean cancelOrder(OrderNoDTO orderNoDTO) {
        OrderInfo orderInfo = findOrderByEpOrderNo(orderNoDTO.getOrderno());
        String resultString = EnpuRequestUtil.requestEnpu(EnpuInterfaceEnum.CANCEL_ORDER.getValue(), JSON.toJSONString(orderNoDTO), enpuConfig);
        if (StringUtils.isNotBlank(resultString)) {
            JSONObject resultObject = JSONObject.parseObject(resultString);
            if (CoreExceptionCodes.SUCCESS.getCode().equals(resultObject.getInteger(EnpuRequestUtil.CODE))) {
                orderInfo.setFlag(OrderStatusEnum.O4.getIndex());
                super.updateById(orderInfo);
                return Boolean.TRUE;
            }
            log.error("订单取消失败，恩普订单号：{}，原因：{}", orderNoDTO.getOrderno(), resultObject.getString("msg"));
            throw new BusinessException(resultObject.getInteger("code"), resultObject.getString("msg"));
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean deleteOrder(String epOrderNo) {
        Wrapper wrapper = Condition.create().eq("ep_order_no", epOrderNo);
        ticketService.deleteOrderTicket(epOrderNo);
        return super.delete(wrapper);
    }

    @Override
    public List<OrderInfo> listOrder(Integer flag) {
        UserInfo userInfo = SessionUtils.getUserInfo();
        Wrapper wrapper = Condition.create().eq("user_id", userInfo.getUserId());
        wrapper.orderBy("add_time", Boolean.FALSE);
        if (flag == 0) {
            return super.selectList(wrapper);
        } else {
            wrapper.eq("flag", flag);
            return super.selectList(wrapper);
        }
    }

    @Override
    public OrderInfo findOrderByEpOrderNo(String epOrderNo) {
        Wrapper wrapper = Condition.create().eq("ep_order_no", epOrderNo);
        return super.selectOne(wrapper);
    }

    @Override
    public ToonPayVO payOrder(OrderNoDTO orderNoDTO) {
        OrderInfo orderInfo = findOrderByEpOrderNo(orderNoDTO.getOrderno());
        if (orderInfo.getFlag() != OrderStatusEnum.O1.getIndex()) {
            throw new BusinessException(ExceptionCode.ORDER_STATUS_ERROR.getCode(), "订单" + OrderStatusEnum.getName(orderInfo.getFlag()));
        }
        String resultString = requestToonPay(orderNoDTO, orderInfo);
        log.info("【聚合支付】响应结果：{}", resultString);
        if (StringUtils.isNotBlank(resultString)) {
            ResponseResult responseResult = JSONObject.parseObject(resultString, ResponseResult.class);
            if (CoreExceptionCodes.SUCCESS.getCode().equals(responseResult.getMeta().getCode())) {
                ToonPayVO toonPayVO = JSON.parseObject(JSONObject.toJSONString(responseResult.getData()), ToonPayVO.class);
                log.info("【聚合支付】支付响应data：{}", toonPayVO);
                orderInfo.setPlatformOrderNo(toonPayVO.getPaltformOrderNo());
                orderInfo.setPayFlag(IS_PAID);
                super.updateById(orderInfo);
                return toonPayVO;
            }
            log.error("订单支付异常，orderNo：{}，epOrderNo：{}，异常原因：{}", orderInfo.getOrderNo(), orderInfo.getEpOrderNo(), responseResult.getMeta().getMessage());
            throw new BusinessException(responseResult.getMeta());
        }
        return new ToonPayVO();
    }

    private String requestToonPay(OrderNoDTO orderNoDTO, OrderInfo orderInfo) {
        JSONObject body = new JSONObject();
        body.put("merNo", toonPayConfig.getMerNo());
        body.put("outOrderNo", orderNoDTO.getOrderno());
        body.put("txAmount", orderInfo.getAmount().multiply(new BigDecimal(100)).intValue() + "");
        body.put("subject", orderInfo.getTicketName());
        body.put("body", orderInfo.getTicketName());
        body.put("tradeType", toonPayConfig.getTradeType());
        body.put("payMethod", toonPayConfig.getPayMethod());
        body.put("mobile", orderInfo.getMobile());
        body.put("userId", orderInfo.getUserId());
        body.put("returnUrl", toonPayConfig.getReturnUrl());
        body.put("notifyUrl", toonPayConfig.getNotifyUrl());

        Map<String, String> header = Maps.newHashMap();
        header.put("Accept", "application/json");
        header.put("Content-type", "application/json; charset=utf-8");
        log.info("【聚合支付】请求URL：{}，body-->{}", toonPayConfig.getCreatePayOrderH5Url(), body.toJSONString());
        return HttpUtils.syncPostString(toonPayConfig.getCreatePayOrderH5Url(), Headers.of(header), body.toJSONString());
    }

    @Override
    public ToonPayCallBackVO payCallback(ToonPayCallBackDTO toonPayCallBackDTO) {
        log.info("【支付回调】入参：{}", JSON.toJSONString(toonPayCallBackDTO));
        OrderInfo orderInfo = findOrderByEpOrderNo(toonPayCallBackDTO.getOutOrderNo());
        ToonPayCallBackVO toonPayCallBackVO = new ToonPayCallBackVO();
        if (StringUtils.equalsIgnoreCase(SUCCESS, toonPayCallBackDTO.getStatus())) {
            orderInfo.setPayNo(toonPayCallBackDTO.getTransactionId());
            // 调用恩普支付接口通知支付成功
            return enpuPay(orderInfo, toonPayCallBackVO);
        }
        toonPayCallBackVO.setCode(toonPayCallBackDTO.getStatus().toUpperCase());
        return toonPayCallBackVO;
    }

    private ToonPayCallBackVO enpuPay(OrderInfo orderInfo, ToonPayCallBackVO toonPayCallBackVO) {
        JSONObject body = new JSONObject();
        body.put("orderno", orderInfo.getEpOrderNo());
        body.put("amount", orderInfo.getAmount());
        body.put("payno", orderInfo.getPayNo());
        JSONObject resultObject;
        Integer code;
        String msg;
        try {
            String resultString = EnpuRequestUtil.requestEnpu(EnpuInterfaceEnum.ORDER_PAY.getValue(), body.toJSONString(), enpuConfig);
            log.info("【恩普票务】支付响应：{}", resultString);
            resultObject = JSONObject.parseObject(resultString);
            code = resultObject.getInteger(EnpuRequestUtil.CODE);
            msg = resultObject.getString("msg");
        } catch (Exception e) {
            log.info("【恩普票务】支付异常，恩普订单号：{}，原因：{}", orderInfo.getEpOrderNo(), e.getMessage());
            handleFailOrder(orderInfo);
            toonPayCallBackVO.setCode(ERROR.toUpperCase());
            return toonPayCallBackVO;
        }
        if (CoreExceptionCodes.SUCCESS.getCode().equals(code)) {
            log.info("【恩普票务】支付成功，恩普订单号：{}", orderInfo.getEpOrderNo());
            handleSuccessOrder(orderInfo);
            toonPayCallBackVO.setCode(SUCCESS.toUpperCase());
        } else if (EnpuRequestUtil.ORDER_PAID.equals(code)) {
            log.info("【恩普票务】订单已支付，无需再次支付，恩普订单号：{}", orderInfo.getEpOrderNo());
            handleSuccessOrder(orderInfo);
            toonPayCallBackVO.setCode(SUCCESS.toUpperCase());
        } else {
            log.info("【恩普票务】支付异常，恩普订单号：{}，原因：{}", orderInfo.getEpOrderNo(), msg);
            handleFailOrder(orderInfo);
            toonPayCallBackVO.setCode(ERROR.toUpperCase());
        }
        return toonPayCallBackVO;
    }

    private void handleSuccessOrder(OrderInfo orderInfo) {
        orderInfo.setFlag(OrderStatusEnum.O2.getIndex());
        orderInfo.setPayTime(System.currentTimeMillis());
        super.updateById(orderInfo);
        // 订单支付成功消息推送
        IMUtil.notice(toonIMService, orderInfo.getUserId(), IMUtil.TICKET_PAY_SUCCESS, IMTypeEnum.TICKET_PURCHASE.getName(),
                orderInfo.getEpOrderNo(), orderInfo.getAmount());
    }

    private void handleFailOrder(OrderInfo orderInfo) {
        orderInfo.setFlag(OrderStatusEnum.O9.getIndex());
        super.updateById(orderInfo);
        if (redisTemplate.opsForHash().putIfAbsent(orderInfo.getUserId(), orderInfo.getEpOrderNo(), ERROR)) {
            // 订单支付异常消息推送
            IMUtil.notice(toonIMService, orderInfo.getUserId(), IMUtil.TICKET_PAY_FAIL, IMTypeEnum.TICKET_PURCHASE.getName(),
                    orderInfo.getEpOrderNo(), orderInfo.getAmount());
        }
    }

    @Override
    public ToonPayResultVO getOrderPayStatus(String orderno) {
        OrderInfo order = findOrderByEpOrderNo(orderno);
        log.info("【聚合支付】查询订单支付状态，订单：{}", order);
        if (order == null) {
            throw new BusinessException(ExceptionCode.ORDER_NOT_EXIST);
        }
        Map<String, String> body = Maps.newHashMap();
        body.put("outTradeNo", order.getPlatformOrderNo());
        String resultString = HttpUtils.syncGetString(toonPayConfig.getTradeOrderDetailUrl(), null, body);
        log.info("【聚合支付】查询订单支付状态接口响应：{}", resultString);
        if (StringUtils.isNotBlank(resultString)) {
            ResponseResult responseResult = JSONObject.parseObject(resultString, ResponseResult.class);
            if (CoreExceptionCodes.SUCCESS.getCode().equals(responseResult.getMeta().getCode())) {
                ToonPayResultVO toonPayResultVO = JSON.parseObject(JSON.toJSONString(responseResult.getData()), ToonPayResultVO.class);
                toonPayResultVO.setAmount(toonPayResultVO.getAmount().divide(new BigDecimal(100)));
                toonPayResultVO.setGroupNo(order.getGroupNo());
                toonPayResultVO.setTicketId(order.getTicketId());
                return toonPayResultVO;
            }
            log.error("【聚合支付】查询订单支付状态异常，恩普订单号：{}，原因：{}", orderno, responseResult.getMeta().getMessage());
            throw new BusinessException(responseResult.getMeta());
        }
        return new ToonPayResultVO();
    }
}