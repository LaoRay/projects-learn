package com.zhengtoon.framework.ticketpurchase.service;

import com.zhengtoon.framework.ticketpurchase.bean.dto.OrderNoDTO;
import com.zhengtoon.framework.ticketpurchase.bean.dto.OrderSaveDTO;
import com.zhengtoon.framework.ticketpurchase.bean.dto.ToonPayCallBackDTO;
import com.zhengtoon.framework.ticketpurchase.bean.vo.*;
import com.zhengtoon.framework.ticketpurchase.entity.OrderInfo;

import java.util.List;

public interface OrderService {

    /**
     * 下单
     *
     * @param orderSaveDTO
     * @return
     */
    OrderSaveVO saveOrder(OrderSaveDTO orderSaveDTO);

    /**
     * 根据订单号查询订单详情
     *
     * @param groupNo
     * @return
     */
    OrderDetailsVO findOrderDetails(Long groupNo);

    /**
     * 取消订单
     *
     * @param orderNoDTO
     * @return
     */
    Boolean cancelOrder(OrderNoDTO orderNoDTO);

    /**
     * 删除订单
     *
     * @param epOrderNo
     * @return
     */
    Boolean deleteOrder(String epOrderNo);

    /**
     * 根据状态查询订单
     *
     * @param flag
     * @return
     */
    List<OrderInfo> listOrder(Integer flag);

    /**
     * 根据恩普票务订单号查询本地订单
     *
     * @param epOrderNo
     * @return
     */
    OrderInfo findOrderByEpOrderNo(String epOrderNo);

    /**
     * 订单支付
     *
     * @param orderNoDTO
     * @return
     */
    ToonPayVO payOrder(OrderNoDTO orderNoDTO);

    /**
     * 支付成功回调
     *
     * @param toonPayCallBackDTO
     * @return
     */
    ToonPayCallBackVO payCallback(ToonPayCallBackDTO toonPayCallBackDTO);

    /**
     * 查询订单支付状态
     *
     * @param orderno
     * @return
     */
    ToonPayResultVO getOrderPayStatus(String orderno);
}
