package com.zhengtoon.framework.annualticketrecharge.service;

/**
 * @Title:${file_name}
 * @Description:
 * @author:hongzhen.chang
 * @date:2018/8/8 10:28
 * @version:1.0
 * @Copyright:2018 www.zhengtoon.com Inc. All rights reserved.
 * @Company:Beijing Siyuan Zhengtoon Technology Group Co., Ltd.
 */

public interface IdGeneratorService {
    /**
     * @param biz 业务名称
     * @return ID
     * @brief Get ID
     */
    long generatorId(String biz);

    /**
     * @return ID
     * @brief Get ID
     */
    long generatorId();

    /**
     * @param id
     * @return ID反译结果
     */
    String parseID(long id);
}

