package com.zhengtoon.framework.ticketpurchase.bean.vo;

import com.zhengtoon.framework.ticketpurchase.bean.dto.TidDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author Leiqiyun
 * @date 2018/8/8 16:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TicketRefundVO extends TidDTO {

    private BigDecimal refundAmount;

    private BigDecimal refundFee;
}
