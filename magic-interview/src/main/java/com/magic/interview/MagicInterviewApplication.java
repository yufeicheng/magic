package com.magic.interview;

import cn.anony.boot.annotation.EnableSms;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author zhuruisong on 2018/4/24
 * @since 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.magic.interview","com.magic.dao"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableSms
public class MagicInterviewApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(MagicInterviewApplication.class).run(args);
    }
}