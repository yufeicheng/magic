package com.magic.interview.service.elegantshutdown;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
