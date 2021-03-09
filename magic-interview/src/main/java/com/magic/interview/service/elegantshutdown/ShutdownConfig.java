package com.magic.interview.service.elegantshutdown;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

/**
 *
 * @author Cheng Yufei
 * @create 2021-03-08 6:12 下午
 **/
@Configuration
public class ShutdownConfig {

    @Autowired
    private CustomizeConfig customizeConfig;

    @Bean(name="servletWebServerFactory")
    public ServletWebServerFactory servletWebServerFactory(){
        TomcatServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory();
        tomcatServletWebServerFactory.addConnectorCustomizers(customizeConfig);
        return tomcatServletWebServerFactory;
    }

    @PreDestroy
    public void back() {
        System.out.println(">>>>关机前，必要事情处理<<<<");
    }
}
