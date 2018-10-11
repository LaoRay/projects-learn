package com.zhengtoon.framework.ticketpurchase.schedule;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.collect.Lists;
import com.zhengtoon.framework.lock.annotions.RedisLock;
import com.zhengtoon.framework.message.im.service.ToonIMService;
import com.zhengtoon.framework.ticketpurchase.common.enums.IMTypeEnum;
import com.zhengtoon.framework.ticketpurchase.common.enums.OrderStatusEnum;
import com.zhengtoon.framework.ticketpurchase.common.enums.TicketStatusEnum;
import com.zhengtoon.framework.ticketpurchase.common.enums.TicketTypeEnum;
import com.zhengtoon.framework.ticketpurchase.common.utils.IMUtil;
import com.zhengtoon.framework.ticketpurchase.common.utils.TicketPurchaseDateUtil;
import com.zhengtoon.framework.ticketpurchase.entity.OrderInfo;
import com.zhengtoon.framework.ticketpurchase.entity.OrderTicket;
import com.zhengtoon.framework.ticketpurchase.mapper.OrderInfoMapper;
import com.zhengtoon.framework.ticketpurchase.mapper.OrderTicketMapper;
import com.zhengtoon.framework.ticketpurchase.service.OrderService;
import com.zhengtoon.framework.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 定时任务, 采用分布式锁的来保证任务不会产生并发
 */
@Slf4j
@Component
@EnableScheduling
public class ScheduledTask {

    private static final Integer INVALID_HOUR = 24;

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderTicketMapper orderTicketMapper;
    @Autowired
    private ToonIMService toonIMService;

    /**
     * 订单超时未支付任务
     */
    @Scheduled(cron = "0 */5 * * * ?")
    @RedisLock(value = "invalidOrderManage")
    public void invalidOrderManage() {
        log.debug("【超时未支付订单失效管理】定时任务开始执行");
        Wrapper wrapper = Condition.create().eq("flag", OrderStatusEnum.O1.getIndex());
        wrapper.lt("add_time", System.currentTimeMillis() - INVALID_HOUR * 60 * 60 * 1000);
        List<OrderInfo> orderList = orderInfoMapper.selectList(wrapper);
        for (OrderInfo orderInfo : orderList) {
            orderInfo.setFlag(OrderStatusEnum.O3.getIndex());
            orderInfoMapper.updateById(orderInfo);
        }
    }

    /**
     * 游览日期为当天且超过18点30分未支付置为已失效
     */
    @Scheduled(cron = "0 30 18 * * ?")
    @RedisLock(value = "invalidOrderManage2")
    public void invalidOrderManage2() {
        log.debug("【游览日期为当天且超过18点30分未支付置为已失效】定时任务开始执行");
        Wrapper wrapper = Condition.create().eq("flag", OrderStatusEnum.O1.getIndex());
        wrapper.eq("use_time", DateUtil.format(new Date(), DateUtil.dayFormat));
        List<OrderTicket> ticketList = orderTicketMapper.selectList(wrapper);
        for (OrderTicket orderTicket : ticketList) {
            EntityWrapper<OrderInfo> ew = new EntityWrapper<>();
            ew.eq("flag", OrderStatusEnum.O1.getIndex());
            ew.eq("ep_order_no", orderTicket.getEpOrderNo());
            OrderInfo orderInfo = orderInfoMapper.selectList(ew).get(0);
            if (orderInfo != null) {
                orderInfo.setFlag(OrderStatusEnum.O3.getIndex());
                orderInfoMapper.updateById(orderInfo);
            }
        }
    }

    /**
     * 门票超时未使用任务
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @RedisLock(value = "invalidTicketManage")
    public void invalidTicketManage() {
        log.debug("【超时未使用门票过期管理】定时任务开始执行");
        List<Integer> statusList = Lists.newArrayList();
        statusList.add(TicketStatusEnum.T2.getIndex());
        statusList.add(TicketStatusEnum.T5.getIndex());
        statusList.add(TicketStatusEnum.T6.getIndex());
        statusList.add(TicketStatusEnum.T7.getIndex());
        Wrapper wrapper = Condition.create().in("flag", statusList);
        wrapper.where("use_time < {0}", DateUtil.format(new Date(), DateUtil.dayFormat));
        List<OrderTicket> ticketList = orderTicketMapper.selectList(wrapper);
        for (OrderTicket ticket : ticketList) {
            ticket.setFlag(TicketStatusEnum.T8.getIndex());
            orderTicketMapper.updateById(ticket);
        }
    }

    /**
     * 支付异常订单任务
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @RedisLock(value = "exceptionOrderManage")
    public void exceptionOrderManage() {
        log.debug("【异常订单管理】定时任务开始执行");
        Wrapper wrapper = Condition.create().eq("flag", OrderStatusEnum.O9.getIndex());
        List<OrderInfo> list = orderInfoMapper.selectList(wrapper);
        log.info("【异常订单: {}】", list);
    }

    /**
     * 游览提醒任务
     */
    @Scheduled(cron = "0 0 18 * * ?")
    @RedisLock(value = "remindVisit")
    public void remindVisit() {
        log.debug("【游览提醒】定时任务开始执行");
        List<Integer> list = Lists.newArrayList();
        list.add(TicketStatusEnum.T2.getIndex());
        list.add(TicketStatusEnum.T5.getIndex());
        list.add(TicketStatusEnum.T6.getIndex());
        list.add(TicketStatusEnum.T7.getIndex());
        Wrapper wrapper = Condition.create().in("flag", list);
        wrapper.eq("use_time", DateUtil.format(DateUtil.addDays(new Date(), 1L), DateUtil.dayFormat));
        List<OrderTicket> ticketList = orderTicketMapper.selectList(wrapper);
        for (OrderTicket ticket : ticketList) {
            // 消息通知
            IMUtil.notice(toonIMService, orderService.findOrderByEpOrderNo(ticket.getEpOrderNo()).getUserId(),
                    IMUtil.VISIT_REMIND, IMTypeEnum.VISIT_REMIND.getName(),
                    TicketPurchaseDateUtil.convertStringFormat(ticket.getUseTime(), DateUtil.dayFormat, TicketPurchaseDateUtil.CN_FORMAT),
                    ticket.getTicketName(), TicketTypeEnum.getName(ticket.getTicketType()), ticket.getManNum());
        }
    }
}
