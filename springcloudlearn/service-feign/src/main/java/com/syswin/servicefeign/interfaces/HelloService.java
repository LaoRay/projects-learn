package com.syswin.servicefeign.interfaces;

import com.syswin.servicefeign.hystrix.HelloServiceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "eureka-client", fallback = HelloServiceHystrix.class)
public interface HelloService {

    @RequestMapping("/hello")
    String hello(@RequestParam("name") String name);
}
