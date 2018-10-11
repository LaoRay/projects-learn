package com.zhengtoon.framework.annualticketrecharge.schedule;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.zhengtoon.framework.annualticketrecharge.common.enums.OrderStatusEnum;
import com.zhengtoon.framework.annualticketrecharge.entity.AnnualTicketOrder;
import com.zhengtoon.framework.annualticketrecharge.mapper.AnnualTicketOrderMapper;
import com.zhengtoon.framework.lock.annotions.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时任务, 采用分布式锁的来保证任务不会产生并发
 */
@Slf4j
@Component
@EnableScheduling
public class ScheduledTask {

    @Autowired
    private AnnualTicketOrderMapper annualTicketOrderMapper;

    private static final Integer INVALID_HOUR = 24;

    /**
     * 订单超时未支付任务
     */
    @Scheduled(cron = "0 */5 * * * ?")
    @RedisLock(value = "invalidOrderManage")
    public void invalidOrderManage() {
        log.debug("【超时未支付订单失效管理】定时任务开始执行");
        Wrapper wrapper = Condition.create().eq("flag", OrderStatusEnum.O1.getIndex());
        wrapper.lt("add_time", System.currentTimeMillis() - INVALID_HOUR * 60 * 60 * 1000);
        List<AnnualTicketOrder> annualTicketOrderList = annualTicketOrderMapper.selectList(wrapper);
        for (AnnualTicketOrder annualTicketOrder : annualTicketOrderList) {
            annualTicketOrder.setFlag(OrderStatusEnum.O3.getIndex());
            annualTicketOrderMapper.updateById(annualTicketOrder);
        }
    }
}
