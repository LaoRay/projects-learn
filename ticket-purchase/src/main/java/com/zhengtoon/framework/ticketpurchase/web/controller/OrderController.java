package com.zhengtoon.framework.ticketpurchase.web.controller;

import com.zhengtoon.framework.entity.ResponseResult;
import com.zhengtoon.framework.ticketpurchase.bean.dto.OrderNoDTO;
import com.zhengtoon.framework.ticketpurchase.bean.dto.OrderSaveDTO;
import com.zhengtoon.framework.ticketpurchase.bean.dto.ToonPayCallBackDTO;
import com.zhengtoon.framework.ticketpurchase.bean.vo.ToonPayCallBackVO;
import com.zhengtoon.framework.ticketpurchase.service.OrderService;
import com.zhengtoon.framework.web.common.WebResCallback;
import com.zhengtoon.framework.web.common.WebResCriteria;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Leiqiyun
 * @date 2018/8/3 9:32
 */
@RestController
@RequestMapping(value = "/order")
@Api(value = "订单管理", description = "订单信息管理")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 下单
     *
     * @param orderSaveDTO
     * @return
     */
    @PostMapping(value = "/save")
    public ResponseResult save(@RequestBody final OrderSaveDTO orderSaveDTO) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(orderService.saveOrder(orderSaveDTO));
            }
        }.sendRequest();
    }

    /**
     * 查询订单详情
     *
     * @param groupNo
     * @return
     */
    @GetMapping(value = "/details/{groupNo}")
    public ResponseResult orderDetails(@PathVariable final Long groupNo) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(orderService.findOrderDetails(groupNo));
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
                criteria.addSingleResult(orderService.cancelOrder(orderNoDTO));
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
    public ResponseResult delete(@RequestBody final OrderNoDTO orderNoDTO) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(orderService.deleteOrder(orderNoDTO.getOrderno()));
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
                criteria.addSingleResult(orderService.listOrder(flag));
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
                criteria.addSingleResult(orderService.payOrder(orderNoDTO));
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
        return orderService.payCallback(toonPayCallBackDTO);
    }

    /**
     * 前端回调查询订单支付状态
     *
     * @param orderno
     * @return
     */
    @GetMapping(value = "/pay/status/{orderno}")
    public ResponseResult orderPayStatus(@PathVariable final String orderno) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(orderService.getOrderPayStatus(orderno));
            }
        }.sendRequest();
    }
}
