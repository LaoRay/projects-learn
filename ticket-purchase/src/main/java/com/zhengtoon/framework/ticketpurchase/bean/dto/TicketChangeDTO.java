package com.zhengtoon.framework.ticketpurchase.bean.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Leiqiyun
 * @date 2018/8/8 16:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TicketChangeDTO extends TidDTO {

    private String usetime;
}
