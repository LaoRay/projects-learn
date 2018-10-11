package com.syswin.consulconsumer.interfaces;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Leiqiyun
 * @date 2018/9/19 16:20
 */
@FeignClient(name = "consul-producer")
public interface HelloService {

    @RequestMapping("/hello")
    String hello(@RequestParam("name") String name);
}
