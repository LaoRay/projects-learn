package com.zhengtoon.framework.ticketpurchase.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.zhengtoon.framework.ticketpurchase.common.component.EnpuConfig;
import com.zhengtoon.framework.utils.DateUtil;
import com.zhengtoon.framework.utils.HttpUtils;
import com.zhengtoon.framework.utils.codec.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;

import java.util.Date;

/**
 * 恩普接口请求工具类
 *
 * @author Leiqiyun
 * @date 2018/8/3 14:08
 */
@Slf4j
public class EnpuRequestUtil {

    /**
     * 恩普票务响应code
     */
    public static final String CODE = "code";
    /**
     * 恩普票务响应状态码--无此订单
     */
    public static final Integer ORDER_NOT_EXIST = 2003;
    /**
     * 恩普票务响应状态码--此订单无需支付
     */
    public static final Integer ORDER_PAID = 2010;

    /**
     * 组装恩普请求头
     *
     * @param cmd
     * @param enpuConfig
     * @return
     */
    public static String assembleHeaderParam(String cmd, EnpuConfig enpuConfig) {
        JSONObject header = new JSONObject();
        header.put("cmd", cmd);
        header.put("partner", enpuConfig.getPartner());
        header.put("signtype", enpuConfig.getSignType());
        header.put("version", enpuConfig.getVersion());
        header.put("createTime", DateUtil.format(new Date(), DateUtil.newFormat));
        return header.toJSONString();
    }

    /**
     * 组装恩普请求参数并发送请求
     *
     * @param cmd
     * @param body
     * @param enpuConfig
     * @return
     */
    public static String requestEnpu(String cmd, String body, EnpuConfig enpuConfig) {
        String header = EnpuRequestUtil.assembleHeaderParam(cmd, enpuConfig);
        String headerString = Base64Utils.encodeToString(header.getBytes());
        String bodyString = Base64Utils.encodeToString(body.getBytes());
        String signed = Md5Utils.md5(enpuConfig.getSignKey() + headerString + bodyString).toUpperCase();
        JSONObject param = new JSONObject();
        param.put("header", headerString);
        param.put("body", bodyString);
        param.put("signed", signed);
        log.info("【恩普票务】请求URL：{}，参数：header：{}，body：{}", cmd, header, body);
        return HttpUtils.syncPostString(enpuConfig.getUrl(), null, param.toJSONString());
    }
}
