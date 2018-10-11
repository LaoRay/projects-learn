package com.zhengtoon.framework.ticketpurchase.bean.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Leiqiyun
 * @date 2018/8/8 17:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderInfoDTO extends OrderNoDTO {

    private Long groupNo;

    private String partnerOrderno;

    private String cnname;

    private String mobile;

    private String idcard;

    private Integer ifoneman;

    private String memo;
}
