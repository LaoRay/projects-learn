package com.zhengtoon.framework.ticketpurchase.bean.dto;

import lombok.Data;

/**
 * @author Leiqiyun
 * @date 2018/8/8 17:47
 */
@Data
public class OrderTicketDTO {

    private Integer ticketid;

    private String ticketName;

    private Integer number;

    private String usetime;
}
