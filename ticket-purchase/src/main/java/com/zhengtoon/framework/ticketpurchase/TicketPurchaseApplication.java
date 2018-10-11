package com.zhengtoon.framework.ticketpurchase;

import com.zhengtoon.framework.disconf.EnableDisconfProperties;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationPid;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@EnableDisconfProperties
@SpringBootApplication
@MapperScan("com.zhengtoon.**.mapper")
public class TicketPurchaseApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder()
                .sources(TicketPurchaseApplication.class)
                .main(TicketPurchaseApplication.class)
                .run(args);
        log.info("----TicketPurchaseApplication Start PID={}----", new ApplicationPid().toString());
        context.registerShutdownHook();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TicketPurchaseApplication.class);
    }
}