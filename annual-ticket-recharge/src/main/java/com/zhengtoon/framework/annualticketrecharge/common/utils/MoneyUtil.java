package com.zhengtoon.framework.annualticketrecharge.common.utils;

import java.math.BigDecimal;

/**
 * @author Leiqiyun
 * @date 2018/9/29 10:12
 */
public class MoneyUtil {

    /**
     * 元转分
     *
     * @param yuan
     * @return
     */
    public static Integer yuan2Fen(BigDecimal yuan) {
        return yuan.movePointRight(2).intValue();
    }

    /**
     * 分转元
     *
     * @param fen
     * @return
     */
    public static BigDecimal fen2Yuan(Integer fen) {
        return new BigDecimal(fen).movePointLeft(2);
    }
}
