package com.zhengtoon.framework.annualticketrecharge.common.wxpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Leiqiyun
 * @date 2018/9/30 15:45
 */
@Data
public class H5SceneInfo {

    /**
     * 该字段用于上报支付的场景信息,针对H5支付有以下三种场景,请根据对应场景上报,
     * H5支付不建议在APP端使用，针对场景1，2请接入APP支付，不然可能会出现兼容性问题
     *
     * 1，IOS移动应用
     * {"h5_info": //h5支付固定传"h5_info"
     *     {"type": "",  //场景类型
     *      "app_name": "",  //应用名
     *      "bundle_id": ""  //bundle_id
     *      }
     * }
     *
     * 2，安卓移动应用
     * {"h5_info": //h5支付固定传"h5_info"
     *     {"type": "",  //场景类型
     *      "app_name": "",  //应用名
     *      "package_name": ""  //包名
     *      }
     * }
     *
     * 3，WAP网站应用
     * {"h5_info": //h5支付固定传"h5_info"
     *    {"type": "",  //场景类型
     *     "wap_url": "",//WAP网站URL地址
     *     "wap_name": ""  //WAP 网站名
     *     }
     * }
     */
    @JSONField(name = "h5_info")
    private H5Info h5Info;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    @Data
    public static class H5Info {
        /**
         * 场景类型
         */
        @JSONField(name = "type")
        private String type;
        /**
         * 应用名
         */
        @JSONField(name = "app_name")
        private String appName;
        /**
         * bundle_id
         */
        @JSONField(name = "bundle_id")
        private String bundleId;
        /**
         * 包名
         */
        @JSONField(name = "package_name")
        private String packageName;
        /**
         * WAP网站URL地址
         */
        @JSONField(name = "wap_url")
        private String wapUrl;
        /**
         * WAP 网站名
         */
        @JSONField(name = "wap_name")
        private String wapName;
    }
}
