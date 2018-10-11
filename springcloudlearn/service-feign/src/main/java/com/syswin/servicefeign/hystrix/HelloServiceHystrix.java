package com.syswin.servicefeign.hystrix;

import com.syswin.servicefeign.interfaces.HelloService;
import org.springframework.stereotype.Component;

/**
 * @author Leiqiyun
 * @date 2018/9/3 16:49
 */
@Component
public class HelloServiceHystrix implements HelloService {

    @Override
    public String hello(String name) {
        return "sorry, " + name;
    }
}
