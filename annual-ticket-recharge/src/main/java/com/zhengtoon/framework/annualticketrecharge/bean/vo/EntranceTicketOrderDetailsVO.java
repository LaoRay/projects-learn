package com.zhengtoon.framework.annualticketrecharge.bean.vo;

import com.zhengtoon.framework.annualticketrecharge.entity.OrderInfo;
import com.zhengtoon.framework.annualticketrecharge.entity.OrderTicket;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Leiqiyun
 * @date 2018/8/9 9:23
 */
@Data
@Builder
public class EntranceTicketOrderDetailsVO {

    private OrderInfo orderInfo;

    private List<OrderTicket> ticketList;
}
