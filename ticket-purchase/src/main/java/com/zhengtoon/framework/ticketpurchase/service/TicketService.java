package com.zhengtoon.framework.ticketpurchase.service;

import com.zhengtoon.framework.ticketpurchase.bean.dto.TicketChangeDTO;
import com.zhengtoon.framework.ticketpurchase.bean.dto.TidDTO;
import com.zhengtoon.framework.ticketpurchase.bean.vo.ScenicTicketIdVO;
import com.zhengtoon.framework.ticketpurchase.bean.vo.TicketRefundVO;
import com.zhengtoon.framework.ticketpurchase.entity.OrderTicket;

import java.util.List;

public interface TicketService {

    /**
     * 获取景区的门票id
     *
     * @return
     */
    ScenicTicketIdVO listTicketId();

    /**
     * 根据产品ID获取票类信息
     *
     * @param productId
     * @return
     */
    List listTicketByProductId(String productId);

    /**
     * 根据票号获取票信息
     *
     * @param tid
     * @return
     */
    OrderTicket getTicketInfo(String tid);

    /**
     * 根据订单号查询门票列表
     *
     * @param orderno
     * @return
     */
    List<OrderTicket> listTicketByOrderNo(String orderno);

    /**
     * 根据恩普票务订单号查询本地订单包含的门票列表
     *
     * @param epOrderNo
     * @return
     */
    List<OrderTicket> getOrderTicketList(String epOrderNo);

    /**
     * 门票改签
     *
     * @param ticketChangeDTO
     * @return
     */
    TicketChangeDTO ticketChange(TicketChangeDTO ticketChangeDTO);

    /**
     * 退票
     *
     * @param tidDTO
     * @return
     */
    TicketRefundVO ticketRefund(TidDTO tidDTO);

    /**
     * 门票删除
     *
     * @param epOrderNo
     * @return
     */
    Boolean deleteOrderTicket(String epOrderNo);
}
