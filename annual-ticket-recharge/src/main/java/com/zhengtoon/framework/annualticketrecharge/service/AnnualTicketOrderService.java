package com.zhengtoon.framework.annualticketrecharge.service;

import com.zhengtoon.framework.annualticketrecharge.bean.dto.OrderNoDTO;
import com.zhengtoon.framework.annualticketrecharge.bean.dto.ToonPayCallBackDTO;
import com.zhengtoon.framework.annualticketrecharge.bean.vo.AnnualTicketOrderDetailsVO;
import com.zhengtoon.framework.annualticketrecharge.bean.vo.ToonPayCallBackVO;
import com.zhengtoon.framework.annualticketrecharge.bean.vo.ToonPayResultVO;
import com.zhengtoon.framework.annualticketrecharge.bean.vo.ToonPayVO;

import java.util.List;

/**
 * @author leiqi
 */
public interface AnnualTicketOrderService {

    /**
     * 添加年票充值订单
     *
     * @param orderNoDTO
     * @return
     */
    AnnualTicketOrderDetailsVO addOrder(OrderNoDTO orderNoDTO);

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
     * @param orderNoDTO
     * @return
     */
    Boolean deleteOrder(OrderNoDTO orderNoDTO);

    /**
     * 查询订单列表
     *
     * @param flag
     * @return
     */
    List<AnnualTicketOrderDetailsVO> listOrder(Integer flag);

    /**
     * 查询订单详情
     *
     * @param orderNo
     * @return
     */
    AnnualTicketOrderDetailsVO findOrderDetails(Long orderNo);

    /**
     * 支付订单
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
     * @param epOrderNo
     * @return
     */
    ToonPayResultVO getOrderPayStatus(String epOrderNo);
}
