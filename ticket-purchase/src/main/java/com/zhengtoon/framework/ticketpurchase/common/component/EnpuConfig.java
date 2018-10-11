package com.zhengtoon.framework.ticketpurchase.common.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Leiqiyun
 * @date 2018/8/3 9:53
 */
@Component
@ConfigurationProperties("enpu")
@Data
public class EnpuConfig {

    private String partner;
    private String signKey;
    private String url;
    private String version;
    private String signType;

    private ScenicConfig scenic;

    @Component
    @ConfigurationProperties("scenic")
    @Data
    public static class ScenicConfig {

        private String mountainResort;
        private String whiteGrass;
        private String grandTheatre;
        private String jinshanMountainGreatWall;
    }
}
