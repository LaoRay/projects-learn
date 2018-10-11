package com.zhengtoon.framework.annualticketrecharge.web.controller;

import com.zhengtoon.framework.annualticketrecharge.bean.dto.PageQueryCriteriaDTO;
import com.zhengtoon.framework.annualticketrecharge.entity.AnnualTicketOrder;
import com.zhengtoon.framework.annualticketrecharge.entity.OrderInfo;
import com.zhengtoon.framework.annualticketrecharge.service.ManagerOrderService;
import com.zhengtoon.framework.entity.ResponseResult;
import com.zhengtoon.framework.web.common.WebResCallback;
import com.zhengtoon.framework.web.common.WebResCriteria;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Leiqiyun
 * @date 2018/9/13 10:55
 */
@RestController
@RequestMapping("manager/order")
@Api(value = "后台票务信息管理", description = "后台票务信息管理")
public class ManagerOrderController {

    @Autowired
    private ManagerOrderService managerOrderService;

    /**
     * 门票购买订单列表查询
     *
     * @param pageQueryCriteriaDTO
     * @return
     */
    @PostMapping(value = "/entranceTicket/list")
    public ResponseResult entranceTicketList(@RequestBody final PageQueryCriteriaDTO<OrderInfo> pageQueryCriteriaDTO) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(managerOrderService.entranceTicketList(pageQueryCriteriaDTO));
            }
        }.sendRequest();
    }

    /**
     * 查询门票购买订单详情
     *
     * @param epOrderNo
     * @return
     */
    @GetMapping(value = "/entranceTicket/details/{epOrderNo}")
    public ResponseResult entranceTicketDetails(@PathVariable final String epOrderNo) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(managerOrderService.entranceTicketDetails(epOrderNo));
            }
        }.sendRequest();
    }

    /**
     * 年票充值订单列表查询
     *
     * @param pageQueryCriteriaDTO
     * @return
     */
    @PostMapping(value = "/annualTicket/list")
    public ResponseResult annualTicketList(@RequestBody final PageQueryCriteriaDTO<AnnualTicketOrder> pageQueryCriteriaDTO) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(managerOrderService.annualTicketList(pageQueryCriteriaDTO));
            }
        }.sendRequest();
    }

    /**
     * 查询年票充值订单详情
     *
     * @param epOrderNo
     * @return
     */
    @GetMapping(value = "/annualTicket/details/{epOrderNo}")
    public ResponseResult annualTicketDetails(@PathVariable final String epOrderNo) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(managerOrderService.annualTicketDetails(epOrderNo));
            }
        }.sendRequest();
    }
}
