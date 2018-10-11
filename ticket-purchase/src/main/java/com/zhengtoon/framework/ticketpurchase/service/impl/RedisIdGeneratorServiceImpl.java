package com.zhengtoon.framework.ticketpurchase.service.impl;

import com.zhengtoon.framework.ticketpurchase.service.IdGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Title:${file_name}
 * @Description:
 * @author:hongzhen.chang
 * @date:2018/8/8 10:27
 * @version:1.0
 * @Copyright:2018 www.zhengtoon.com Inc. All rights reserved.
 * @Company:Beijing Siyuan Zhengtoon Technology Group Co., Ltd.
 */
@Slf4j
@Service("redisIdGeneratorService")
public class RedisIdGeneratorServiceImpl implements IdGeneratorService {

    private static final String keyPrefix = "smart";

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * @return
     * @brief:获取ID前缀
     */
    private String getIDPrefix() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        // 今天是第几天
        int day = c.get(Calendar.DAY_OF_YEAR);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        // 0补位操作 必须满足三位
        String dayFmt = String.format("%1$03d", day);
        // 0补位操作 必须满足2位
        String hourFmt = String.format("%1$02d", hour);
        // 0补位操作 必须满足2位
        String minuteFmt = String.format("%1$02d", minute);
        // 0补位操作 必须满足2位
        String secondFmt = String.format("%1$02d", second);
        StringBuffer prefix = new StringBuffer();
        prefix.append((year - 2000)).append(dayFmt).append(hourFmt).append(minuteFmt).append(secondFmt);
        return prefix.toString();
    }


    /**
     * @return Id
     * @brief:通过调用redis的increment api实现自增获取
     */
    private long incrGeneId(String biz) {
        String prefix = getIDPrefix();
        log.info("getIDPrefix:{}", prefix);
        String orderId = null;
        // 00001
        String key = "#{biz}:id:".replace("#{biz}", biz).concat(prefix);
        try {
            ValueOperations<String, Object> valueOper = redisTemplate.opsForValue();
            Long index = valueOper.increment(key, 1);
            // 补位操作 保证满足5位
            orderId = prefix.concat(String.format("%1$05d", index));
        } catch (Exception ex) {
            log.error("request Enpu ticket interface failure exception...", ex);
        } finally {
            //保留10分钟内的key
            redisTemplate.expire(key, 600, TimeUnit.SECONDS);
        }
        if (StringUtils.isEmpty(orderId)) {
            return 0;
        }
        return Long.parseLong(orderId);
    }

    /**
     * @param biz 业务名称
     * @return Id
     * @Description 获取票据ID
     * <il>带入参，票据业务编码</il>
     */
    @Override
    public long generatorId(String biz) {
        // 转成数字类型，可排序
        return incrGeneId(biz);
    }

    /**
     * @return Id
     * @Description 获取票据ID
     * <li>不带入参</li>
     */
    @Override
    public long generatorId() {
        return incrGeneId(keyPrefix);
    }


    @Override
    public String parseID(long id) {
        return null;
    }
}