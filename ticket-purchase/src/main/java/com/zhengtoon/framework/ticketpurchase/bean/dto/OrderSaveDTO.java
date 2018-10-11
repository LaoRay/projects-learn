package com.zhengtoon.framework.ticketpurchase.bean.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Leiqiyun
 * @date 2018/8/8 17:50
 */
@Data
public class OrderSaveDTO {

    private OrderInfoDTO orderinfo;

    private List<OrderTicketDTO> ticket;
}
