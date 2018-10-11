package com.zhengtoon.framework.annualticketrecharge.common.utils;

import com.zhengtoon.framework.utils.codec.Sm4Util;
import lombok.extern.slf4j.Slf4j;

/**
 * Sm4j加密解密工具
 *
 * @author leiqiyun
 **/
@Slf4j
public class Sm4Utils {

    public static final String DEFAULT_SECRET_KEY = "EA600AA74EF19CD959087CC37BBD8B41";
    private static final String CHARSET_UTF8 = "UTF8";
    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * sm4 加密
     *
     * @param data      待加密数据
     * @param secretKey 加密key
     * @return 加密后数据
     */
    public static String encryptEcbHex(String data, String secretKey) {
        try {
            byte[] result = Sm4Util.encrypt_Ecb_Padding(hexStringToBytes(secretKey), data.getBytes(CHARSET_UTF8));
            return bytesToHexString(result);
        } catch (Exception e) {
            log.error("sm4 encrypt error", e);
            return data;
        }
    }

    /**
     * sm4 解密
     *
     * @param data      加密后数据
     * @param secretKey 加密key
     * @return 解密后数据
     */
    public static String decryptEcbHex(String data, String secretKey) {
        try {
            byte[] result = Sm4Util.decrypt_Ecb_Padding(hexStringToBytes(secretKey), hexStringToBytes(data));
            return new String(result);
        } catch (Exception e) {
            log.error("sm4 decrypt error", e);
            return data;
        }
    }

    /**
     * 字节数组转16进制
     *
     * @param bytes 需要转换的byte数组
     * @return 转换后的Hex字符串
     */
    private static String bytesToHexString(byte[] bytes) {
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for (byte b : bytes) {
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }
        return new String(buf);
    }

    /**
     * 16进制转字节数组
     *
     * @param hexString 需要转换的16进制
     * @return 转换后的字节数组
     */
    private static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || "".equals(hexString.trim())) {
            return new byte[0];
        }
        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length() / 2; i++) {
            String subStr = hexString.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }
}
