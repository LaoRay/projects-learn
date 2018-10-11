package com.zhengtoon.framework.annualticketrecharge.bean.dto;

import lombok.Data;

/**
 * @author Leiqiyun
 * @date 2018/8/8 17:40
 */
@Data
public class ToonPayCallBackDTO {

    private String outOrderNo;

    private String transactionId;

    private String status;
}
