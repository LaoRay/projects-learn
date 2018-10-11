package com.zhengtoon.framework.ticketpurchase.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.zhengtoon.framework.exception.BusinessException;
import com.zhengtoon.framework.exception.CoreExceptionCodes;
import com.zhengtoon.framework.jwt.common.SessionUtils;
import com.zhengtoon.framework.message.im.service.ToonIMService;
import com.zhengtoon.framework.ticketpurchase.bean.dto.TicketChangeDTO;
import com.zhengtoon.framework.ticketpurchase.bean.dto.TidDTO;
import com.zhengtoon.framework.ticketpurchase.bean.vo.ScenicTicketIdVO;
import com.zhengtoon.framework.ticketpurchase.bean.vo.TicketRefundVO;
import com.zhengtoon.framework.ticketpurchase.common.component.EnpuConfig;
import com.zhengtoon.framework.ticketpurchase.common.enums.EnpuInterfaceEnum;
import com.zhengtoon.framework.ticketpurchase.common.enums.IMTypeEnum;
import com.zhengtoon.framework.ticketpurchase.common.enums.TicketStatusEnum;
import com.zhengtoon.framework.ticketpurchase.common.enums.TicketTypeEnum;
import com.zhengtoon.framework.ticketpurchase.common.exception.ExceptionCode;
import com.zhengtoon.framework.ticketpurchase.common.utils.EnpuRequestUtil;
import com.zhengtoon.framework.ticketpurchase.common.utils.IMUtil;
import com.zhengtoon.framework.ticketpurchase.common.utils.TicketPurchaseDateUtil;
import com.zhengtoon.framework.ticketpurchase.entity.OrderTicket;
import com.zhengtoon.framework.ticketpurchase.mapper.OrderInfoMapper;
import com.zhengtoon.framework.ticketpurchase.mapper.OrderTicketMapper;
import com.zhengtoon.framework.ticketpurchase.service.TicketService;
import com.zhengtoon.framework.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @author Leiqiyun
 * @date 2018/8/3 9:33
 */
@Service
@Slf4j
public class TicketServiceImpl extends ServiceImpl<OrderTicketMapper, OrderTicket> implements TicketService {

    @Autowired
    private EnpuConfig enpuConfig;
//    @Autowired
//    private EnpuConfig.ScenicConfig scenicConfig;
    @Autowired
    private ToonIMService toonIMService;
    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Value("${ticket.refund.time}")
    private String ticketRefundTime;

    @Override
    public ScenicTicketIdVO listTicketId() {
        ScenicTicketIdVO scenicTicketIdVO = new ScenicTicketIdVO();
        scenicTicketIdVO.setMountainResort(enpuConfig.getScenic().getMountainResort());
        scenicTicketIdVO.setWhiteGrass(enpuConfig.getScenic().getWhiteGrass());
        scenicTicketIdVO.setGrandTheatre(enpuConfig.getScenic().getGrandTheatre());
        scenicTicketIdVO.setJinshanMountainGreatWall(enpuConfig.getScenic().getJinshanMountainGreatWall());
        return scenicTicketIdVO;
    }

    @Override
    public List listTicketByProductId(String productId) {
        JSONObject body = new JSONObject();
        body.put("productId", productId);
        String resultString = EnpuRequestUtil.requestEnpu(EnpuInterfaceEnum.GET_PRODUCT.getValue(), body.toJSONString(), enpuConfig);
        if (StringUtils.isNotBlank(resultString)) {
            JSONObject resultObject = JSONObject.parseObject(resultString);
            if (CoreExceptionCodes.SUCCESS.getCode().equals(resultObject.getInteger(EnpuRequestUtil.CODE))) {
                return JSON.parseArray(new String(Base64Utils.decodeFromString(resultObject.getString("body"))));
            }
            log.error("查询景区门票列表失败，原因：{}", resultObject.getString("msg"));
            throw new BusinessException(resultObject.getInteger("code"), resultObject.getString("msg"));
        }
        return Lists.newArrayList();
    }

    @Override
    public List<OrderTicket> listTicketByOrderNo(String epOrderNo) {
        JSONObject body = new JSONObject();
        body.put("orderno", epOrderNo);
        String resultString = EnpuRequestUtil.requestEnpu(EnpuInterfaceEnum.SEARCH_ORDER_DETAILS.getValue(), body.toJSONString(), enpuConfig);
        if (StringUtils.isNotBlank(resultString)) {
            JSONObject resultObject = JSONObject.parseObject(resultString);
            if (CoreExceptionCodes.SUCCESS.getCode().equals(resultObject.getInteger(EnpuRequestUtil.CODE))) {
                return getTicketList(epOrderNo, resultObject);
            }
            // 恩普订单已不存在，但本地订单还在
            if (EnpuRequestUtil.ORDER_NOT_EXIST.equals(resultObject.getInteger(EnpuRequestUtil.CODE))) {
                return getOrderTicketList(epOrderNo);
            }
            log.error("根据订单查询门票信息失败，恩普订单号：{}，原因：{}", epOrderNo, resultObject.getString("msg"));
            throw new BusinessException(resultObject.getInteger("code"), resultObject.getString("msg"));
        }
        return Lists.newArrayList();
    }

    private List<OrderTicket> getTicketList(String epOrderNo, JSONObject resultObject) {
        try {
            JSONObject epOrder = JSONObject.parseObject(new String(Base64Utils.decodeFromString(resultObject.getString("body"))));
            List<OrderTicket> ticketList = transferToOrderTicketList(epOrder);
            List<OrderTicket> list = getOrderTicketList(epOrderNo);
            if (CollectionUtils.isEmpty(list)) {
                super.insertBatch(ticketList);
                return ticketList;
            } else {
                updateTicketStatus(ticketList, list);
                return list;
            }
        } catch (Exception e) {
            log.error("【恩普票务】根据订单查询门票详情接口异常-->{}", e);
            return getOrderTicketList(epOrderNo);
        }
    }

    private List<OrderTicket> transferToOrderTicketList(JSONObject epOrder) {
        JSONObject orders = epOrder.getJSONObject("orders");
        JSONArray ticketArray = epOrder.getJSONArray("ticket");
        List<OrderTicket> ticketList = Lists.newArrayList();
        for (int i = 0; i < ticketArray.size(); i++) {
            JSONObject jsonObject = ticketArray.getJSONObject(i);
            OrderTicket orderTicket = jsonToOrderTicket(jsonObject);
            orderTicket.setEpOrderNo(orders.getString("orderno"));
            orderTicket.setOrderNo(orders.getString("partnerOrderno"));
            ticketList.add(orderTicket);
        }
        return ticketList;
    }

    private OrderTicket jsonToOrderTicket(JSONObject json) {
        OrderTicket ticket = new OrderTicket();
        ticket.setTid(json.getString("tid"));
        ticket.setTicketName(json.getString("ticketname"));
        ticket.setTicketType(TicketTypeEnum.getIndex(json.getString("tickettype")));
        ticket.setSummary(json.getString("summary"));
        ticket.setSinglePrice(json.getBigDecimal("singleprice"));
        ticket.setTprice(json.getBigDecimal("tprice"));
        ticket.setManNum(json.getInteger("mannum"));
        ticket.setUseTime(json.getString("usetime").replaceAll("-", "/"));
        ticket.setFlag(StringUtils.isBlank(json.getString("flag")) ? TicketStatusEnum.T1.getIndex() : TicketStatusEnum.getIndex(json.getString("flag")));
        ticket.setQr(json.getString("qr"));
        ticket.setQrStream(json.getString("qrStream"));
        ticket.setIfTaopiao(json.getInteger("ifTaopiao"));
        ticket.setExchangeCode(json.getString("exchangeCode"));
        return ticket;
    }

    private void updateTicketStatus(List<OrderTicket> epList, List<OrderTicket> list) {
        if (CollectionUtils.isEmpty(epList) || CollectionUtils.isEmpty(list)) {
            return;
        }
        boolean needUpdate;
        for (OrderTicket epTicket : epList) {
            for (OrderTicket ticket : list) {
                needUpdate = StringUtils.equals(epTicket.getTid(), ticket.getTid())
                        && epTicket.getFlag() != null && ticket.getFlag() < TicketStatusEnum.T7.getIndex()
                        && !ticket.getFlag().equals(epTicket.getFlag());
                if (needUpdate) {
                    ticket.setFlag(epTicket.getFlag());
                    super.updateById(ticket);
                    break;
                }
            }
        }
    }

    @Override
    public OrderTicket getTicketInfo(String tid) {
        JSONObject body = new JSONObject();
        body.put("tid", tid);
        String resultString = EnpuRequestUtil.requestEnpu(EnpuInterfaceEnum.SEARCH_TICKET.getValue(), body.toJSONString(), enpuConfig);
        if (StringUtils.isNotBlank(resultString)) {
            JSONObject resultObject = JSONObject.parseObject(resultString);
            if (CoreExceptionCodes.SUCCESS.getCode().equals(resultObject.getInteger(EnpuRequestUtil.CODE))) {
                JSONObject jsonObject = JSONObject.parseObject(new String(Base64Utils.decodeFromString(resultObject.getString("body"))));
                return jsonToOrderTicket(jsonObject.getJSONObject("ticket"));
            }
            if (EnpuRequestUtil.ORDER_NOT_EXIST.equals(resultObject.getInteger(EnpuRequestUtil.CODE))) {
                return super.selectOne(Condition.create().eq("tid", tid));
            }
            log.error("查询门票凭证失败，票号：{}，原因：{}", tid, resultObject.getString("msg"));
            throw new BusinessException(resultObject.getInteger("code"), resultObject.getString("msg"));
        }
        return new OrderTicket();
    }

    @Override
    public List<OrderTicket> getOrderTicketList(String epOrderNo) {
        Wrapper wrapper = Condition.create().eq("ep_order_no", epOrderNo);
        return super.selectList(wrapper);
    }

    @Override
    public TicketChangeDTO ticketChange(TicketChangeDTO ticketChangeDTO) {
        String resultString = EnpuRequestUtil.requestEnpu(EnpuInterfaceEnum.TICKET_CHANGE.getValue(), JSON.toJSONString(ticketChangeDTO), enpuConfig);
        if (StringUtils.isNotBlank(resultString)) {
            JSONObject resultObject = JSONObject.parseObject(resultString);
            if (CoreExceptionCodes.SUCCESS.getCode().equals(resultObject.getInteger(EnpuRequestUtil.CODE))) {
                TicketChangeDTO ticket = JSONObject.parseObject(new String(Base64Utils.decodeFromString(resultObject.getString("body"))), TicketChangeDTO.class);
                OrderTicket orderTicket = findTicketByTid(ticket.getTid());
                orderTicket.setUseTime(ticket.getUsetime().replaceAll("-", "/"));
                orderTicket.setFlag(TicketStatusEnum.T7.getIndex());
                orderTicket.setChangeTime(System.currentTimeMillis());
                updateById(orderTicket);
                // 消息通知
                IMUtil.notice(toonIMService, SessionUtils.getUserInfo().getUserId(), IMUtil.TICKET_CHANGE, IMTypeEnum.TICKET_CHANGE.getName(),
                        orderTicket.getTicketName(), orderTicket.getManNum(), TicketTypeEnum.getName(orderTicket.getTicketType()),
                        TicketPurchaseDateUtil.convertStringFormat(orderTicket.getUseTime(), DateUtil.dayFormat, TicketPurchaseDateUtil.CN_FORMAT));
                return ticket;
            }
            log.error("改签失败，票号：{}，原因：{}", ticketChangeDTO.getTid(), resultObject.getString("msg"));
            throw new BusinessException(resultObject.getInteger("code"), resultObject.getString("msg"));
        }
        return new TicketChangeDTO();
    }

    private OrderTicket findTicketByTid(String tid) {
        Wrapper wrapper = Condition.create().eq("tid", tid);
        return super.selectOne(wrapper);
    }

    @Override
    public TicketRefundVO ticketRefund(TidDTO tidDTO) {
        if (isOverflowSixteen(tidDTO)) {
            throw new BusinessException(ExceptionCode.OVER_USE_TIME);
        }
        String resultString = EnpuRequestUtil.requestEnpu(EnpuInterfaceEnum.TICKET_REFUND.getValue(), JSON.toJSONString(tidDTO), enpuConfig);
        if (StringUtils.isNotBlank(resultString)) {
            JSONObject resultObject = JSONObject.parseObject(resultString);
            if (CoreExceptionCodes.SUCCESS.getCode().equals(resultObject.getInteger(EnpuRequestUtil.CODE))) {
                TicketRefundVO ticketRefundVO = JSONObject.parseObject(new String(Base64Utils.decodeFromString(resultObject.getString("body"))), TicketRefundVO.class);
                Wrapper wrapper = Condition.create().eq("tid", ticketRefundVO.getTid());
                OrderTicket orderTicket = super.selectOne(wrapper);
                orderTicket.setFlag(TicketStatusEnum.T3.getIndex());
                orderTicket.setRefundTime(System.currentTimeMillis());
                updateById(orderTicket);
                // 消息通知
                IMUtil.notice(toonIMService, SessionUtils.getUserInfo().getUserId(), IMUtil.TICKET_REFUND, IMTypeEnum.TICKET_REFUND.getName(),
                        orderTicket.getTicketName(),
                        TicketPurchaseDateUtil.convertStringFormat(orderTicket.getUseTime(), DateUtil.dayFormat, TicketPurchaseDateUtil.CN_FORMAT),
                        TicketTypeEnum.getName(orderTicket.getTicketType()),
                        orderTicket.getManNum(), ticketRefundVO.getRefundAmount(), ticketRefundVO.getRefundFee());
                return ticketRefundVO;
            }
            log.error("退票失败，票号：{}，原因：{}", tidDTO.getTid(), resultObject.getString("msg"));
            throw new BusinessException(resultObject.getInteger("code"), resultObject.getString("msg"));
        }
        return new TicketRefundVO();
    }

    private Boolean isOverflowSixteen(TidDTO tidDTO) {
        OrderTicket ticket = findTicketByTid(tidDTO.getTid());
        Date useDate;
        try {
            useDate = DateUtil.parseDateNoTime(ticket.getUseTime().concat(" ").concat(ticketRefundTime), TicketPurchaseDateUtil.FORMAT);
        } catch (ParseException e) {
            log.error("【退票】日期转换异常，原因：{}", e);
            throw new BusinessException(ExceptionCode.SERVICE_BUSY);
        }
        if (System.currentTimeMillis() > useDate.getTime()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean deleteOrderTicket(String epOrderNo) {
        Wrapper wrapper = Condition.create().eq("ep_order_no", epOrderNo);
        return super.delete(wrapper);
    }
}
