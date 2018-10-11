package com.zhengtoon.framework.ticketpurchase.common.utils;

import com.google.common.collect.Maps;
import com.zhengtoon.framework.utils.DateUtil;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static com.zhengtoon.framework.utils.DateUtil.convert;
import static com.zhengtoon.framework.utils.DateUtil.getNewDateFormat;

/**
 * @author Leiqiyun
 * @date 2018/8/29 8:58
 */
public class TicketPurchaseDateUtil {

    private static final Integer LOW_SEASON_MONTH = 3;
    private static final Integer BUSY_SEASON_MONTH = 10;
    private static final Integer LAST_DAY_OF_MONTH = 31;
    private static final Integer SIXTEEN = 16;
    private static final Integer LAST_HOUR_OF_DAY = 23;
    private static final Integer LAST_MINUTE_OF_HOUR = 59;
    private static final Integer LAST_SECOND_OF_MINUTE = 59;

    public static final String FORMAT = "yyyy/MM/dd HH:mm:ss";
    public static final String CN_FORMAT = "yyyy年MM月dd日";

    /**
     * 获取门票购买时间范围
     *
     * @return
     */
    public static Map getTicketPurchaseTimeRange() {
        Map<String, Object> map = Maps.newHashMap();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        Date currentDate = cal.getTime();
        map.put("currentDay", DateUtil.format(currentDate, DateUtil.dayFormat));
        map.put("currentHour", hour);
        map.put("currentTimeMillis", System.currentTimeMillis());
        cal.clear();
        cal.set(year, BUSY_SEASON_MONTH - 1, LAST_DAY_OF_MONTH, LAST_HOUR_OF_DAY, LAST_MINUTE_OF_HOUR, LAST_SECOND_OF_MINUTE);
        Date busyLimitDay = cal.getTime();
        cal.clear();
        cal.set(year, LOW_SEASON_MONTH - 1, LAST_DAY_OF_MONTH, LAST_HOUR_OF_DAY, LAST_MINUTE_OF_HOUR, LAST_SECOND_OF_MINUTE);
        Date lowLimitDay = cal.getTime();
        cal.clear();
        cal.set(Calendar.YEAR, year);
        cal.roll(Calendar.DAY_OF_YEAR, -1);
        cal.set(Calendar.HOUR_OF_DAY, LAST_HOUR_OF_DAY);
        cal.set(Calendar.MINUTE, LAST_MINUTE_OF_HOUR);
        cal.set(Calendar.SECOND, LAST_SECOND_OF_MINUTE);
        Date lastDayOfYear = cal.getTime();
        if (currentDate.before(busyLimitDay) && currentDate.after(lowLimitDay)) {
            map.put("endTime", DateUtil.format(busyLimitDay, DateUtil.dayFormat));
        } else if (currentDate.after(busyLimitDay) && currentDate.before(lastDayOfYear)) {
            // 10月31日23点59分59秒之后 ~ 12月31日24点之前
            cal.clear();
            cal.setTime(lowLimitDay);
            cal.add(Calendar.YEAR, 1);
            Date addYear = cal.getTime();
            map.put("endTime", DateUtil.format(addYear, DateUtil.dayFormat));
        } else {
            // 12月31日23点59分59秒之后 ~ 3月31日24点之前
            map.put("endTime", DateUtil.format(lowLimitDay, DateUtil.dayFormat));
        }
        // 判断当前日期是否为淡季或旺季最后一天
        boolean isLimitDay = (month == BUSY_SEASON_MONTH || month == LOW_SEASON_MONTH) && dayOfMonth == LAST_DAY_OF_MONTH;
        if (hour >= SIXTEEN && !isLimitDay) {
            map.put("startTime", DateUtil.format(DateUtil.addDays(currentDate, 1), DateUtil.dayFormat));
        } else {
            map.put("startTime", DateUtil.format(currentDate, DateUtil.dayFormat));
        }
        return map;
    }

    public static String convertStringFormat(String dateString, String formatIn, String formatOut) {
        DateFormat df1 = getNewDateFormat(formatIn);
        DateFormat df2 = getNewDateFormat(formatOut);
        return convert(dateString, df1, df2);
    }
}
