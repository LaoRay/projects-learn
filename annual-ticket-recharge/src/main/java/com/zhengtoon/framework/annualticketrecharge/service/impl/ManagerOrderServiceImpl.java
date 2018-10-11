package com.zhengtoon.framework.annualticketrecharge.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhengtoon.framework.annualticketrecharge.bean.dto.PageQueryCriteriaDTO;
import com.zhengtoon.framework.annualticketrecharge.bean.vo.EntranceTicketOrderDetailsVO;
import com.zhengtoon.framework.annualticketrecharge.common.exception.ExceptionCode;
import com.zhengtoon.framework.annualticketrecharge.common.utils.SecurityUtil;
import com.zhengtoon.framework.annualticketrecharge.common.utils.Sm4Utils;
import com.zhengtoon.framework.annualticketrecharge.entity.AnnualTicketOrder;
import com.zhengtoon.framework.annualticketrecharge.entity.OrderInfo;
import com.zhengtoon.framework.annualticketrecharge.entity.OrderTicket;
import com.zhengtoon.framework.annualticketrecharge.mapper.AnnualTicketOrderMapper;
import com.zhengtoon.framework.annualticketrecharge.mapper.OrderInfoMapper;
import com.zhengtoon.framework.annualticketrecharge.mapper.OrderTicketMapper;
import com.zhengtoon.framework.annualticketrecharge.service.ManagerOrderService;
import com.zhengtoon.framework.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Leiqiyun
 * @date 2018/9/13 11:03
 */
@Service
@Slf4j
public class ManagerOrderServiceImpl implements ManagerOrderService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private AnnualTicketOrderMapper annualTicketOrderMapper;
    @Autowired
    private OrderTicketMapper orderTicketMapper;

    @Override
    public Page<OrderInfo> entranceTicketList(PageQueryCriteriaDTO<OrderInfo> page) {
        EntityWrapper<OrderInfo> ew = new EntityWrapper<>();
        ew.like(StringUtils.isNotBlank(page.getEpOrderNo()),
                "ep_order_no", page.getEpOrderNo());
        ew.like(StringUtils.isNotBlank(page.getMobile()),
                "mobile", page.getMobile());
        ew.where(page.getFlag() != null && page.getFlag() != 0,
                "flag = {0}", page.getFlag());
        ew.where(page.getAddTimeStart() != null && page.getAddTimeStart() != 0,
                "add_time >= {0}", page.getAddTimeStart());
        ew.where(page.getAddTimeEnd() != null && page.getAddTimeEnd() != 0,
                "add_time <= {0}", page.getAddTimeEnd());
        ew.orderBy("add_time", false);
        List<OrderInfo> list = orderInfoMapper.selectPage(page, ew);
        page.setRecords(list);
        return page;
    }

    @Override
    public EntranceTicketOrderDetailsVO entranceTicketDetails(String epOrderNo) {
        EntityWrapper<OrderInfo> orderInfoEntityWrapper = new EntityWrapper<>();
        orderInfoEntityWrapper.eq("ep_order_no", epOrderNo);
        OrderInfo orderInfo = orderInfoMapper.selectList(orderInfoEntityWrapper).get(0);
        if (orderInfo == null) {
            throw new BusinessException(ExceptionCode.ORDER_NOT_EXIST);
        }
        EntityWrapper<OrderTicket> orderTicketEntityWrapper = new EntityWrapper<>();
        orderTicketEntityWrapper.eq("ep_order_no", epOrderNo);
        List<OrderTicket> orderTicketList = orderTicketMapper.selectList(orderTicketEntityWrapper);
        return EntranceTicketOrderDetailsVO.builder().orderInfo(orderInfo).ticketList(orderTicketList).build();
    }

    @Override
    public Page<AnnualTicketOrder> annualTicketList(PageQueryCriteriaDTO<AnnualTicketOrder> page) {
        EntityWrapper<AnnualTicketOrder> ew = new EntityWrapper<>();
        ew.like(StringUtils.isNotBlank(page.getEpOrderNo()),
                "ep_order_no", page.getEpOrderNo());
        ew.like(StringUtils.isNotBlank(page.getMobile()),
                "mobile", page.getMobile());
        ew.where(page.getFlag() != null && page.getFlag() != 0,
                "flag = {0}", page.getFlag());
        ew.where(page.getAddTimeStart() != null && page.getAddTimeStart() != 0,
                "add_time >= {0}", page.getAddTimeStart());
        ew.where(page.getAddTimeEnd() != null && page.getAddTimeEnd() != 0,
                "add_time <= {0}", page.getAddTimeEnd());
        ew.orderBy("add_time", false);
        List<AnnualTicketOrder> list = annualTicketOrderMapper.selectPage(page, ew);
        page.setRecords(list);
        return page;
    }

    @Override
    public AnnualTicketOrder annualTicketDetails(String epOrderNo) {
        EntityWrapper<AnnualTicketOrder> annualTicketOrderEntityWrapper = new EntityWrapper<>();
        annualTicketOrderEntityWrapper.eq("ep_order_no", epOrderNo);
        AnnualTicketOrder annualTicketOrder = annualTicketOrderMapper.selectList(annualTicketOrderEntityWrapper).get(0);
        annualTicketOrder.setIdCard(SecurityUtil.maskSensitiveInfo(Sm4Utils.decryptEcbHex(annualTicketOrder.getIdCard(),
                Sm4Utils.DEFAULT_SECRET_KEY), 6, 14));
        return annualTicketOrder;
    }
}
