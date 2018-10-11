package com.zhengtoon.framework.annualticketrecharge.web.interceptor;

import com.zhengtoon.framework.jwt.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author leiqi
 */
@Configuration
public class WebAppConfigurer extends WebMvcConfigurerAdapter {

    @Autowired
    private JwtInterceptor jwtInterceptor;
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/annualTicket/order/**")
                .addPathPatterns("/user/**")
                .excludePathPatterns("/annualTicket/order/pay/callback")
                .excludePathPatterns("/wx/**")
                .excludePathPatterns("/v1/open/**")
                .excludePathPatterns("/v2/open/**");
//        registry.addInterceptor(loginInterceptor)
//                .addPathPatterns("/manager/**")
//                .excludePathPatterns("/manager/user/login")
//                .excludePathPatterns("/manager/user/register")
//                .excludePathPatterns("/image/render");
    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setMaxAge(3600L);
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }
}
