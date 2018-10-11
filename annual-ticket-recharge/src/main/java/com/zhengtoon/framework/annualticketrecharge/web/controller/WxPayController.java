package com.zhengtoon.framework.annualticketrecharge.web.controller;

import com.zhengtoon.framework.annualticketrecharge.common.enums.PayTypeEnum;
import com.zhengtoon.framework.annualticketrecharge.common.utils.XmlUtil;
import com.zhengtoon.framework.annualticketrecharge.common.wxpay.PayRequest;
import com.zhengtoon.framework.annualticketrecharge.common.wxpay.QueryRequest;
import com.zhengtoon.framework.annualticketrecharge.common.wxpay.WxPayConstants;
import com.zhengtoon.framework.annualticketrecharge.service.IdGeneratorService;
import com.zhengtoon.framework.annualticketrecharge.service.WxPayService;
import com.zhengtoon.framework.entity.ResponseResult;
import com.zhengtoon.framework.web.common.WebResCallback;
import com.zhengtoon.framework.web.common.WebResCriteria;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

/**
 * @author Leiqiyun
 * @date 2018/9/28 10:20
 */
@RestController
@Slf4j
@RequestMapping("/v2/open")
public class WxPayController {

    @Autowired
    @Qualifier("redisIdGeneratorService")
    private IdGeneratorService redisIdGeneratorService;
    @Autowired
    private WxPayService wxPayService;

    @PostMapping("/pay")
    public ResponseResult pay(@RequestBody final PayRequest payRequest) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                payRequest.setOrderAmount(new BigDecimal(0.01));
                payRequest.setOrderNo(redisIdGeneratorService.generatorId() + "");
                payRequest.setOrderName("测试支付");
                payRequest.setPayTypeEnum(PayTypeEnum.WXPAY_MWEB);
                log.info("【发起微信支付】payRequest={}", payRequest);
                criteria.addSingleResult(wxPayService.pay(payRequest));
            }
        }.sendRequest();
    }

    /**
     * 支付异步回调
     */
    @PostMapping("/weChat/notify")
    @SneakyThrows
    public void notify(HttpServletRequest request, HttpServletResponse response) {
        log.info("【微信支付异步回调】request={}", request);
        String notifyData = XmlUtil.parseXmlToString(request);
        log.info("【微信支付异步回调】notifyData={}", notifyData);
        wxPayService.asyncNotify(notifyData);
        response.getWriter().write(XmlUtil.setXML(WxPayConstants.SUCCESS, WxPayConstants.OK));
    }

    /**
     * 查询订单
     *
     * @param outTradeNo
     * @return
     */
    @GetMapping("/queryOrder")
    public ResponseResult queryOrder(@RequestParam("outTradeNo") final String outTradeNo) {
        log.info("【微信查询订单】订单号：outTradeNo={}", outTradeNo);
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                QueryRequest queryRequest = new QueryRequest();
                queryRequest.setOutTradeNo(outTradeNo);
                criteria.addSingleResult(wxPayService.queryOrder(queryRequest));
            }
        }.sendRequest();
    }
}
