package com.zhengtoon.framework.annualticketrecharge.web.controller;

import com.zhengtoon.framework.annualticketrecharge.bean.dto.OrderNoDTO;
import com.zhengtoon.framework.annualticketrecharge.bean.dto.ToonPayCallBackDTO;
import com.zhengtoon.framework.annualticketrecharge.bean.vo.ToonPayCallBackVO;
import com.zhengtoon.framework.annualticketrecharge.service.AnnualTicketOrderService;
import com.zhengtoon.framework.entity.ResponseResult;
import com.zhengtoon.framework.web.common.WebResCallback;
import com.zhengtoon.framework.web.common.WebResCriteria;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Leiqiyun
 * @date 2018/9/12 11:32
 */
@RestController
@RequestMapping("annualTicket/order")
@Api(value = "年票充值订单管理", description = "年票充值订单管理")
public class AnnualTicketOrderController {

    @Autowired
    private AnnualTicketOrderService annualTicketOrderService;

    /**
     * 下单
     *
     * @param orderNoDTO
     * @return
     */
    @PostMapping(value = "/add")
    public ResponseResult addOrder(@RequestBody final OrderNoDTO orderNoDTO) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(annualTicketOrderService.addOrder(orderNoDTO));
            }
        }.sendRequest();
    }

    /**
     * 取消未支付订单
     *
     * @param orderNoDTO
     * @return
     */
    @PostMapping(value = "/cancel")
    public ResponseResult cancel(@RequestBody final OrderNoDTO orderNoDTO) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(annualTicketOrderService.cancelOrder(orderNoDTO));
            }
        }.sendRequest();
    }

    /**
     * 删除订单
     *
     * @param orderNoDTO
     * @return
     */
    @PostMapping(value = "/delete")
    public ResponseResult deleteOrder(@RequestBody final OrderNoDTO orderNoDTO) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(annualTicketOrderService.deleteOrder(orderNoDTO));
            }
        }.sendRequest();
    }

    /**
     * 根据订单状态查询订单列表 0全部 1未支付 2已支付 3已失效 4已取消
     *
     * @param flag
     * @return
     */
    @GetMapping(value = "/list/{flag}")
    public ResponseResult list(@PathVariable final Integer flag) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(annualTicketOrderService.listOrder(flag));
            }
        }.sendRequest();
    }

    /**
     * 查询订单详情
     *
     * @param orderNo
     * @return
     */
    @GetMapping(value = "/details/{orderNo}")
    public ResponseResult orderDetails(@PathVariable final Long orderNo) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(annualTicketOrderService.findOrderDetails(orderNo));
            }
        }.sendRequest();
    }

    /**
     * 订单支付
     *
     * @param orderNoDTO
     * @return
     */
    @PostMapping(value = "/pay")
    public ResponseResult pay(@RequestBody final OrderNoDTO orderNoDTO) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(annualTicketOrderService.payOrder(orderNoDTO));
            }
        }.sendRequest();
    }

    /**
     * 支付回调
     *
     * @param toonPayCallBackDTO
     * @return
     */
    @PostMapping(value = "/pay/callback")
    public ToonPayCallBackVO payCallback(final ToonPayCallBackDTO toonPayCallBackDTO) {
        return annualTicketOrderService.payCallback(toonPayCallBackDTO);
    }

    /**
     * 前端回调查询订单支付状态
     *
     * @param epOrderNo
     * @return
     */
    @GetMapping(value = "/pay/status/{epOrderNo}")
    public ResponseResult orderPayStatus(@PathVariable final String epOrderNo) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(annualTicketOrderService.getOrderPayStatus(epOrderNo));
            }
        }.sendRequest();
    }
}
