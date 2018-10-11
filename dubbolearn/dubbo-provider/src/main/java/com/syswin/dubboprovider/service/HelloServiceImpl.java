package com.syswin.dubboprovider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.syswin.dubboapi.HelloService;

/**
 * @author Leiqiyun
 * @date 2018/9/5 17:19
 */
@Service(version = "${demo.service.version}",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}")
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String name) {
        return "hello " + name;
    }
}
