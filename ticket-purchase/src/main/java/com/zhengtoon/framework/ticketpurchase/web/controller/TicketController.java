package com.zhengtoon.framework.ticketpurchase.web.controller;

import com.zhengtoon.framework.entity.ResponseResult;
import com.zhengtoon.framework.ticketpurchase.bean.dto.TicketChangeDTO;
import com.zhengtoon.framework.ticketpurchase.bean.dto.TidDTO;
import com.zhengtoon.framework.ticketpurchase.service.TicketService;
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
@RequestMapping(value = "/ticket")
@Api(value = "门票管理", description = "门票信息管理")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    /**
     * 获取景区门票id
     *
     * @return
     */
    @GetMapping("/listTicketId")
    public ResponseResult listTicketId() {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(ticketService.listTicketId());
            }
        }.sendRequest();
    }

    /**
     * 获取票类信息
     *
     * @param productId
     * @return
     */
    @GetMapping(value = "/listByProductId")
    public ResponseResult listTicketByProductId(@RequestParam(required = false, defaultValue = "") final String productId) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(ticketService.listTicketByProductId(productId));
            }
        }.sendRequest();
    }

    /**
     * 根据恩普票务订单号查询门票列表
     *
     * @param orderno
     * @return
     */
    @GetMapping(value = "/listByOrderNo/{orderno}")
    public ResponseResult listTicketByOrderNo(@PathVariable final String orderno) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(ticketService.listTicketByOrderNo(orderno));
            }
        }.sendRequest();
    }

    /**
     * 根据票号获取门票凭证信息
     *
     * @param tid
     * @return
     */
    @GetMapping(value = "/info/{tid}")
    public ResponseResult getTicketInfo(@PathVariable final String tid) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(ticketService.getTicketInfo(tid));
            }
        }.sendRequest();
    }

    /**
     * 门票改签
     *
     * @param ticketChangeDTO
     * @return
     */
    @PostMapping(value = "/change")
    public ResponseResult change(@RequestBody final TicketChangeDTO ticketChangeDTO) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(ticketService.ticketChange(ticketChangeDTO));
            }
        }.sendRequest();
    }

    /**
     * 退票
     *
     * @param tidDTO
     * @return
     */
    @PostMapping(value = "/refund")
    public ResponseResult refund(@RequestBody final TidDTO tidDTO) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(ticketService.ticketRefund(tidDTO));
            }
        }.sendRequest();
    }
}
