package com.zhengtoon.framework.annualticketrecharge.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Leiqiyun
 * @date 2018/9/7 11:49
 */
public class SecurityUtil {

    /**
     * 掩码敏感信息
     *
     * @param content 内容
     * @param start   开始位置
     * @param end     结束位置
     * @return 掩码身份证
     */
    public static String maskSensitiveInfo(String content, int start, int end) {
        if (start > end) {
            return "";
        }
        return new StringBuffer(content).replace(start, end,
                StringUtils.repeat("*", end - start)).toString();
    }
}
