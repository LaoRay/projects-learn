package com.zhengtoon.framework.annualticketrecharge.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zhengtoon.framework.annualticketrecharge.bean.dto.OrderNoDTO;
import com.zhengtoon.framework.annualticketrecharge.bean.dto.ToonPayCallBackDTO;
import com.zhengtoon.framework.annualticketrecharge.bean.vo.AnnualTicketOrderDetailsVO;
import com.zhengtoon.framework.annualticketrecharge.bean.vo.ToonPayCallBackVO;
import com.zhengtoon.framework.annualticketrecharge.bean.vo.ToonPayResultVO;
import com.zhengtoon.framework.annualticketrecharge.bean.vo.ToonPayVO;
import com.zhengtoon.framework.annualticketrecharge.common.component.EnpuConfig;
import com.zhengtoon.framework.annualticketrecharge.common.component.ToonPayConfig;
import com.zhengtoon.framework.annualticketrecharge.common.enums.EnpuInterfaceEnum;
import com.zhengtoon.framework.annualticketrecharge.common.enums.IMTypeEnum;
import com.zhengtoon.framework.annualticketrecharge.common.enums.OrderStatusEnum;
import com.zhengtoon.framework.annualticketrecharge.common.exception.ExceptionCode;
import com.zhengtoon.framework.annualticketrecharge.common.utils.EnpuRequestUtil;
import com.zhengtoon.framework.annualticketrecharge.common.utils.IMUtil;
import com.zhengtoon.framework.annualticketrecharge.common.utils.SecurityUtil;
import com.zhengtoon.framework.annualticketrecharge.common.utils.Sm4Utils;
import com.zhengtoon.framework.annualticketrecharge.entity.AnnualTicketOrder;
import com.zhengtoon.framework.annualticketrecharge.mapper.AnnualTicketOrderMapper;
import com.zhengtoon.framework.annualticketrecharge.service.AnnualTicketOrderService;
import com.zhengtoon.framework.annualticketrecharge.service.IdGeneratorService;
import com.zhengtoon.framework.entity.ResponseResult;
import com.zhengtoon.framework.exception.BusinessException;
import com.zhengtoon.framework.exception.CoreExceptionCodes;
import com.zhengtoon.framework.jwt.bean.dto.UserInfo;
import com.zhengtoon.framework.jwt.common.SessionUtils;
import com.zhengtoon.framework.message.im.service.ToonIMService;
import com.zhengtoon.framework.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
 * @date 2018/9/12 11:37
 */
@Service
@Slf4j
public class AnnualTicketOrderServiceImpl extends ServiceImpl<AnnualTicketOrderMapper, AnnualTicketOrder> implements AnnualTicketOrderService {

    @Autowired
    private EnpuConfig enpuConfig;
    @Autowired
    private ToonPayConfig toonPayConfig;
    @Autowired
    private ToonIMService toonIMService;
    @Autowired
    @Qualifier("redisIdGeneratorService")
    private IdGeneratorService redisIdGeneratorService;
    @Autowired
    private RedisTemplate redisTemplate;

    private static final String SUCCESS = "success";
    private static final String ERROR = "error";
    private static final Integer NO_PAID = 0;
    private static final Integer HAS_PAID = 1;

    @Override
    public AnnualTicketOrderDetailsVO addOrder(OrderNoDTO orderNoDTO) {
        if (orderNoDTO == null) {
            orderNoDTO = new OrderNoDTO();
        }
        UserInfo userInfo = SessionUtils.getUserInfo();
        // TODO 测试用，待删除
        userInfo.setCertName("张一");
        userInfo.setCertNo("130821198102081111");

        if (StringUtils.isNotBlank(orderNoDTO.getEpOrderNo())) {
            deleteOrder(orderNoDTO);
        }
        Long orderNo = orderNoDTO.getOrderNo();
        if (orderNo == null || orderNo == 0) {
            orderNo = redisIdGeneratorService.generatorId();
        }
        JSONObject body = new JSONObject();
        body.put("cnname", userInfo.getCertName());
        body.put("idcard", userInfo.getCertNo());
        String resultString = EnpuRequestUtil.requestEnpu(EnpuInterfaceEnum.ADD_NP_ORDER.getValue(), JSON.toJSONString(body), enpuConfig);
        if (StringUtils.isNotBlank(resultString)) {
            JSONObject resultObject = JSONObject.parseObject(resultString);
            if (CoreExceptionCodes.SUCCESS.getCode().equals(resultObject.getInteger(EnpuRequestUtil.CODE))) {
                JSONObject resultJson = JSONObject.parseObject(new String(Base64Utils.decodeFromString(resultObject.getString("body"))));
                log.info("【恩普票务】下单响应body：{}", resultJson);
                AnnualTicketOrder annualTicketOrder = assembleAnnualTicketOrder(resultJson, orderNo, userInfo);
                super.insert(annualTicketOrder);
                AnnualTicketOrderDetailsVO annualTicketOrderDetailsVO = new AnnualTicketOrderDetailsVO();
                BeanUtils.copyProperties(annualTicketOrder, annualTicketOrderDetailsVO);
                annualTicketOrderDetailsVO.setIdCard(maskIdCard(userInfo.getCertNo()));
                return annualTicketOrderDetailsVO;
            }
            log.error("下单失败，原因：{}", resultObject.getString("msg"));
            throw new BusinessException(resultObject.getInteger("code"), resultObject.getString("msg"));
        }
        return new AnnualTicketOrderDetailsVO();
    }

    private AnnualTicketOrder assembleAnnualTicketOrder(JSONObject resultJson, Long orderNo, UserInfo userInfo) {
        AnnualTicketOrder annualTicketOrder = new AnnualTicketOrder();
        annualTicketOrder.setOrderNo(orderNo);
        annualTicketOrder.setEpOrderNo(resultJson.getString("orderno"));
        annualTicketOrder.setAmount(resultJson.getBigDecimal("amount"));
        annualTicketOrder.setAddTime(System.currentTimeMillis());
        annualTicketOrder.setFlag(OrderStatusEnum.O1.getIndex());
        annualTicketOrder.setPayFlag(NO_PAID);
        annualTicketOrder.setCardType(resultJson.getString("cardtype"));
        annualTicketOrder.setValidTerm(resultJson.getString("validtermAfter"));
        annualTicketOrder.setCardName("避暑山庄景区-普通年票");
        annualTicketOrder.setUserId(userInfo.getUserId());
        annualTicketOrder.setCnName(userInfo.getCertName());
        annualTicketOrder.setIdCard(Sm4Utils.encryptEcbHex(userInfo.getCertNo(), Sm4Utils.DEFAULT_SECRET_KEY));
        annualTicketOrder.setMobile(userInfo.getMobile());
        return annualTicketOrder;
    }

    @Override
    public Boolean cancelOrder(OrderNoDTO orderNoDTO) {
        AnnualTicketOrder annualTicketOrder = findOrderByEpOrderNo(orderNoDTO.getEpOrderNo());
        if (annualTicketOrder == null) {
            throw new BusinessException(ExceptionCode.ORDER_NOT_EXIST);
        }
        annualTicketOrder.setFlag(OrderStatusEnum.O4.getIndex());
        return super.updateById(annualTicketOrder);
    }

    private AnnualTicketOrder findOrderByEpOrderNo(String epOrderNo) {
        Wrapper wrapper = Condition.create().eq("ep_order_no", epOrderNo);
        return super.selectOne(wrapper);
    }

    @Override
    public Boolean deleteOrder(OrderNoDTO orderNoDTO) {
        Wrapper wrapper = Condition.create().eq("ep_order_no", orderNoDTO.getEpOrderNo());
        return super.delete(wrapper);
    }

    @Override
    public List<AnnualTicketOrderDetailsVO> listOrder(Integer flag) {
        UserInfo userInfo = SessionUtils.getUserInfo();
        Wrapper wrapper = Condition.create().eq("user_id", userInfo.getUserId());
        wrapper.orderBy("add_time", Boolean.FALSE);
        List<AnnualTicketOrder> annualTicketOrderList;
        if (flag == 0) {
            annualTicketOrderList = super.selectList(wrapper);
        } else {
            wrapper.eq("flag", flag);
            annualTicketOrderList = super.selectList(wrapper);
        }
        return convert2OrderDetail(annualTicketOrderList);
    }

    private List<AnnualTicketOrderDetailsVO> convert2OrderDetail(List<AnnualTicketOrder> annualTicketOrderList) {
        List<AnnualTicketOrderDetailsVO> orderDetailList = Lists.newArrayList();
        for (AnnualTicketOrder annualTicketOrder : annualTicketOrderList) {
            AnnualTicketOrderDetailsVO annualTicketOrderDetailsVO = new AnnualTicketOrderDetailsVO();
            BeanUtils.copyProperties(annualTicketOrder, annualTicketOrderDetailsVO);
            annualTicketOrderDetailsVO.setIdCard(maskIdCard(Sm4Utils.decryptEcbHex(annualTicketOrder.getIdCard(), Sm4Utils.DEFAULT_SECRET_KEY)));
            orderDetailList.add(annualTicketOrderDetailsVO);
        }
        return orderDetailList;
    }

    private String maskIdCard(String idCard) {
        return SecurityUtil.maskSensitiveInfo(idCard, 6, 14);
    }

    @Override
    public AnnualTicketOrderDetailsVO findOrderDetails(Long orderNo) {
        Wrapper wrapper = Condition.create().eq("order_no", orderNo);
        AnnualTicketOrder annualTicketOrder = super.selectOne(wrapper);
        if (annualTicketOrder == null) {
            throw new BusinessException(ExceptionCode.ORDER_NOT_EXIST);
        }
        AnnualTicketOrderDetailsVO annualTicketOrderDetailsVO = new AnnualTicketOrderDetailsVO();
        BeanUtils.copyProperties(annualTicketOrder, annualTicketOrderDetailsVO);
        annualTicketOrderDetailsVO.setIdCard(maskIdCard(Sm4Utils.decryptEcbHex(annualTicketOrder.getIdCard(), Sm4Utils.DEFAULT_SECRET_KEY)));
        return annualTicketOrderDetailsVO;
    }

    @Override
    public ToonPayVO payOrder(OrderNoDTO orderNoDTO) {
        AnnualTicketOrder annualTicketOrder = findOrderByEpOrderNo(orderNoDTO.getEpOrderNo());
        if (annualTicketOrder == null) {
            log.error("订单支付--订单不存在，恩普订单号：{}", orderNoDTO.getEpOrderNo());
            throw new BusinessException(ExceptionCode.ORDER_NOT_EXIST);
        }
        if (annualTicketOrder.getFlag() != OrderStatusEnum.O1.getIndex()) {
            throw new BusinessException(ExceptionCode.ORDER_STATUS_ERROR.getCode(), "订单" + OrderStatusEnum.getName(annualTicketOrder.getFlag()));
        }
        String resultString = requestToonPay(orderNoDTO, annualTicketOrder);
        log.info("【聚合支付】响应结果：{}", resultString);
        ToonPayVO toonPayVO = new ToonPayVO();
        if (StringUtils.isNotBlank(resultString)) {
            ResponseResult responseResult = JSONObject.parseObject(resultString, ResponseResult.class);
            if (CoreExceptionCodes.SUCCESS.getCode().equals(responseResult.getMeta().getCode())) {
                JSONObject resultJson = JSON.parseObject(JSONObject.toJSONString(responseResult.getData()));
                log.info("【聚合支付】支付响应data：{}", resultJson);
                toonPayVO.setEpOrderNo(resultJson.getString("outOrderNo"));
                toonPayVO.setPlatformOrderNo(resultJson.getString("paltformOrderNo"));
                toonPayVO.setUrl(resultJson.getString("url"));

                annualTicketOrder.setPlatformOrderNo(toonPayVO.getPlatformOrderNo());
                annualTicketOrder.setPayFlag(HAS_PAID);
                super.updateById(annualTicketOrder);
                return toonPayVO;
            }
            log.error("订单支付异常，orderNo：{}，epOrderNo：{}，异常原因：{}", annualTicketOrder.getOrderNo(), annualTicketOrder.getEpOrderNo(), responseResult.getMeta().getMessage());
            throw new BusinessException(responseResult.getMeta());
        }
        return toonPayVO;
    }

    private String requestToonPay(OrderNoDTO orderNoDTO, AnnualTicketOrder annualTicketOrder) {
        JSONObject body = new JSONObject();
        body.put("merNo", toonPayConfig.getMerNo());
        body.put("outOrderNo", orderNoDTO.getEpOrderNo());
        body.put("txAmount", annualTicketOrder.getAmount().multiply(new BigDecimal(100)).intValue() + "");
        body.put("subject", annualTicketOrder.getCardName());
        body.put("body", annualTicketOrder.getCardName());
        body.put("tradeType", toonPayConfig.getTradeType());
        body.put("payMethod", toonPayConfig.getPayMethod());
        body.put("mobile", annualTicketOrder.getMobile());
        body.put("userId", annualTicketOrder.getUserId());
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
        AnnualTicketOrder annualTicketOrder = findOrderByEpOrderNo(toonPayCallBackDTO.getOutOrderNo());
        ToonPayCallBackVO toonPayCallBackVO = new ToonPayCallBackVO();
        if (StringUtils.equalsIgnoreCase(SUCCESS, toonPayCallBackDTO.getStatus())) {
            annualTicketOrder.setPayNo(toonPayCallBackDTO.getTransactionId());
            // 调用恩普支付接口通知支付成功
            return enpuPay(annualTicketOrder, toonPayCallBackVO);
        }
        toonPayCallBackVO.setCode(toonPayCallBackDTO.getStatus().toUpperCase());
        return toonPayCallBackVO;
    }

    private ToonPayCallBackVO enpuPay(AnnualTicketOrder annualTicketOrder, ToonPayCallBackVO toonPayCallBackVO) {
        JSONObject body = new JSONObject();
        body.put("orderno", annualTicketOrder.getEpOrderNo());
        body.put("amount", annualTicketOrder.getAmount());
        body.put("payno", annualTicketOrder.getPayNo());
        JSONObject resultObject;
        Integer code;
        String msg;
        try {
            String resultString = EnpuRequestUtil.requestEnpu(EnpuInterfaceEnum.NP_ORDER_PAY.getValue(), body.toJSONString(), enpuConfig);
            log.info("【恩普票务】支付响应：{}", resultString);
            resultObject = JSONObject.parseObject(resultString);
            code = resultObject.getInteger(EnpuRequestUtil.CODE);
            msg = resultObject.getString("msg");
        } catch (Exception e) {
            log.info("【恩普票务】支付异常，恩普订单号：{}，原因：{}", annualTicketOrder.getEpOrderNo(), e.getMessage());
            handleFailOrder(annualTicketOrder);
            toonPayCallBackVO.setCode(ERROR.toUpperCase());
            return toonPayCallBackVO;
        }
        if (CoreExceptionCodes.SUCCESS.getCode().equals(code)) {
            log.info("【恩普票务】支付成功，恩普订单号：{}", annualTicketOrder.getEpOrderNo());
            handleSuccessOrder(annualTicketOrder);
            toonPayCallBackVO.setCode(SUCCESS.toUpperCase());
        } else if (EnpuRequestUtil.ORDER_PAID.equals(code)) {
            log.info("【恩普票务】订单已支付，无需再次支付，恩普订单号：{}", annualTicketOrder.getEpOrderNo());
            handleSuccessOrder(annualTicketOrder);
            toonPayCallBackVO.setCode(SUCCESS.toUpperCase());
        } else {
            log.info("【恩普票务】支付异常，恩普订单号：{}，原因：{}", annualTicketOrder.getEpOrderNo(), msg);
            handleFailOrder(annualTicketOrder);
            toonPayCallBackVO.setCode(ERROR.toUpperCase());
        }
        return toonPayCallBackVO;
    }

    private void handleSuccessOrder(AnnualTicketOrder annualTicketOrder) {
        annualTicketOrder.setFlag(OrderStatusEnum.O2.getIndex());
        annualTicketOrder.setPayTime(System.currentTimeMillis());
        super.updateById(annualTicketOrder);
        // 订单支付成功消息推送
        IMUtil.notice(toonIMService, annualTicketOrder.getUserId(), IMUtil.RECHARGE_SUCCESS, IMTypeEnum.ANNUAL_TICKET_RECHARGE.getName(),
                annualTicketOrder.getEpOrderNo(), annualTicketOrder.getAmount(), annualTicketOrder.getValidTerm());
    }

    private void handleFailOrder(AnnualTicketOrder annualTicketOrder) {
        annualTicketOrder.setFlag(OrderStatusEnum.O9.getIndex());
        super.updateById(annualTicketOrder);
        if (redisTemplate.opsForHash().putIfAbsent(annualTicketOrder.getUserId(), annualTicketOrder.getEpOrderNo(), ERROR)) {
            // 订单支付异常消息推送
            IMUtil.notice(toonIMService, annualTicketOrder.getUserId(), IMUtil.RECHARGE_FAIL, IMTypeEnum.ANNUAL_TICKET_RECHARGE.getName(),
                    annualTicketOrder.getEpOrderNo(), annualTicketOrder.getAmount());
        }
    }

    @Override
    public ToonPayResultVO getOrderPayStatus(String epOrderNo) {
        AnnualTicketOrder annualTicketOrder = findOrderByEpOrderNo(epOrderNo);
        log.info("【聚合支付】查询订单支付状态，订单：{}", annualTicketOrder);
        if (annualTicketOrder == null) {
            throw new BusinessException(ExceptionCode.ORDER_NOT_EXIST);
        }
        Map<String, String> body = Maps.newHashMap();
        body.put("outTradeNo", annualTicketOrder.getPlatformOrderNo());
        String resultString = HttpUtils.syncGetString(toonPayConfig.getTradeOrderDetailUrl(), null, body);
        log.info("【聚合支付】查询订单支付状态接口响应：{}", resultString);
        if (StringUtils.isNotBlank(resultString)) {
            ResponseResult responseResult = JSONObject.parseObject(resultString, ResponseResult.class);
            if (CoreExceptionCodes.SUCCESS.getCode().equals(responseResult.getMeta().getCode())) {
                ToonPayResultVO toonPayResultVO = JSON.parseObject(JSON.toJSONString(responseResult.getData()), ToonPayResultVO.class);
                toonPayResultVO.setAmount(toonPayResultVO.getAmount().divide(new BigDecimal(100)));
                toonPayResultVO.setOrderNo(annualTicketOrder.getOrderNo());
                return toonPayResultVO;
            }
            log.error("【聚合支付】查询订单支付状态异常，恩普订单号：{}，原因：{}", epOrderNo, responseResult.getMeta().getMessage());
            throw new BusinessException(responseResult.getMeta());
        }
        return new ToonPayResultVO();
    }
}
