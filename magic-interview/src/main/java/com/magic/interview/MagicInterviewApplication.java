package com.magic.interview;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author zhuruisong on 2018/4/24
 * @since 1.0
 */
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class MagicInterviewApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(MagicInterviewApplication.class).run(args);
    }
}