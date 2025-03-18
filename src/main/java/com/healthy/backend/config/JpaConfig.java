package com.healthy.backend.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.filter.RequestContextFilter;

@Configuration
public class JpaConfig {

    @Bean
    public FilterRegistrationBean<OpenEntityManagerInViewFilter> openEntityManagerInViewFilter() {
        FilterRegistrationBean<OpenEntityManagerInViewFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        OpenEntityManagerInViewFilter filter = new OpenEntityManagerInViewFilter();
        filter.setEntityManagerFactoryBeanName("entityManagerFactory");
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<RequestContextFilter> requestContextFilter() {
        FilterRegistrationBean<RequestContextFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new RequestContextFilter());
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
} 