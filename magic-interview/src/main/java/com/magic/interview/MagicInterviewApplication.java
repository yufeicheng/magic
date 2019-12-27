package com.magic.interview;

import cn.anony.boot.annotation.EnableSms;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

/**
 * @author zhuruisong on 2018/4/24
 * @since 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.magic.interview","com.magic.dao"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableSms
//获取配置文件加密盐值
//@PropertySource(value = {"file:D:/encrypt.properties"})
@PropertySource(value = {"file:/data/encrypt.properties"})
public class MagicInterviewApplication {

    @Value("${jasypt.encryptor.password}")
    private String saltValue;

    public static void main(String[] args) {
        new SpringApplicationBuilder(MagicInterviewApplication.class).run(args);
    }

    /**
     *设置jasypt加/解密类
     * @return
     */
    @Bean
    public BasicTextEncryptor basicTextEncryptor() {
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(saltValue);
        return encryptor;
    }
}