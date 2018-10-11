package com.zhengtoon.framework.annualticketrecharge.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhengtoon.framework.annualticketrecharge.bean.dto.PageQueryCriteriaDTO;
import com.zhengtoon.framework.annualticketrecharge.bean.vo.EntranceTicketOrderDetailsVO;
import com.zhengtoon.framework.annualticketrecharge.entity.AnnualTicketOrder;
import com.zhengtoon.framework.annualticketrecharge.entity.OrderInfo;

/**
 * @author Leiqiyun
 * @date 2018/9/13 11:03
 */
public interface ManagerOrderService {
    /**
     * 根据条件查询门票购买订单列表
     *
     * @param pageQueryCriteriaDTO
     * @return
     */
    Page<OrderInfo> entranceTicketList(PageQueryCriteriaDTO<OrderInfo> pageQueryCriteriaDTO);

    /**
     * 查询门票购买订单详情
     *
     * @param epOrderNo
     * @return
     */
    EntranceTicketOrderDetailsVO entranceTicketDetails(String epOrderNo);

    /**
     * 根据条件查询年票充值订单列表
     *
     * @param pageQueryCriteriaDTO
     * @return
     */
    Page<AnnualTicketOrder> annualTicketList(PageQueryCriteriaDTO<AnnualTicketOrder> pageQueryCriteriaDTO);

    /**
     * 查询年票充值订单详情
     *
     * @param epOrderNo
     * @return
     */
    AnnualTicketOrder annualTicketDetails(String epOrderNo);
}
