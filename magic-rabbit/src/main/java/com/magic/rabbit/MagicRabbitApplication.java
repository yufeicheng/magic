package com.magic.rabbit;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author
 * @since 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.magic.rabbit","com.magic.dao"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@PropertySource(value = {"file:D:/encrypt.properties"})
@EnableScheduling
public class MagicRabbitApplication {

    @Value("${jasypt.encryptor.password}")
    private String saltValue;

    public static void main(String[] args) {
        new SpringApplicationBuilder(MagicRabbitApplication.class).run(args);
    }

    @Bean
    public BasicTextEncryptor getBasicTextEncryptor() {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(saltValue);
        return textEncryptor;
    }

}