package com.zhengtoon.framework.ticketpurchase.bean.vo;

import com.zhengtoon.framework.ticketpurchase.entity.OrderInfo;
import com.zhengtoon.framework.ticketpurchase.entity.OrderTicket;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Leiqiyun
 * @date 2018/8/9 9:23
 */
@Data
@Builder
public class OrderDetailsVO {

    private OrderInfo orderInfo;

    private List<OrderTicket> ticketList;
}
