package com.zhengtoon.framework.annualticketrecharge.bean.dto;

import com.baomidou.mybatisplus.plugins.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Leiqiyun
 * @date 2018/9/13 11:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageQueryCriteriaDTO<T> extends Page<T> {

    private String epOrderNo;

    private String mobile;

    private Integer flag;

    private Long addTimeStart;

    private Long addTimeEnd;
}
