package com.zhengtoon.framework.annualticketrecharge;

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
public class AnnualTicketRechargeApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder()
                .sources(AnnualTicketRechargeApplication.class)
                .main(AnnualTicketRechargeApplication.class)
                .run(args);
        log.info("----AnnualTicketRechargeApplication Start PID={}----", new ApplicationPid().toString());
        context.registerShutdownHook();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AnnualTicketRechargeApplication.class);
    }
}