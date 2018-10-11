package com.zhengtoon.framework.ticketpurchase.bean.vo;

import com.zhengtoon.framework.ticketpurchase.bean.dto.OrderNoDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author Leiqiyun
 * @date 2018/8/8 17:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderSaveVO extends OrderNoDTO {

    private Long groupNo;

    private String partnerOrderno;

    private BigDecimal amount;

    private String payno;
}
