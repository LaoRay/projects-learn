package com.zhengtoon.framework.annualticketrecharge.common.exception;

/**
 * @Title:${file_name}
 * @Description:
 * @author:hongzhen.chang
 * @date:2018/8/8 10:58
 * @version:1.0
 * @Copyright:2018 www.zhengtoon.com Inc. All rights reserved.
 * @Company:Beijing Siyuan Zhengtoon Technology Group Co., Ltd.
 */
public class IdGeneratorException extends RuntimeException {

    /**
     * Serial Version ID
     */


    /**
     * Default constructor
     */
    public IdGeneratorException() {
        super();
    }

    /**
     * Constructor with message & cause
     *
     * @param message
     * @param cause
     */
    public IdGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with message
     *
     * @param message
     */
    public IdGeneratorException(String message) {
        super(message);
    }

    /**
     * Constructor with message format
     *
     * @param msgFormat
     * @param args
     */
    public IdGeneratorException(String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
    }

    /**
     * Constructor with cause
     *
     * @param cause
     */
    public IdGeneratorException(Throwable cause) {
        super(cause);
    }
}