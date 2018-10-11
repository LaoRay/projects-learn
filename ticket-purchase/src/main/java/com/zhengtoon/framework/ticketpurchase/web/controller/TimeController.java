package com.zhengtoon.framework.ticketpurchase.web.controller;

import com.zhengtoon.framework.entity.ResponseResult;
import com.zhengtoon.framework.ticketpurchase.common.utils.TicketPurchaseDateUtil;
import com.zhengtoon.framework.web.common.WebResCallback;
import com.zhengtoon.framework.web.common.WebResCriteria;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Leiqiyun
 * @date 2018/8/28 19:07
 */
@RestController
@RequestMapping("time")
public class TimeController {

    /**
     * 获取系统时间
     *
     * @return
     */
    @GetMapping(value = "/range")
    public ResponseResult cancel() {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(TicketPurchaseDateUtil.getTicketPurchaseTimeRange());
            }
        }.sendRequest();
    }
}
