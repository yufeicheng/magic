package com.magic.api.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Cheng Yufei
 * @create 2019-08-12 21:49
 **/
@Component
@ConfigurationProperties(prefix = "db.stock")
@Getter
@Setter
public class StockConfiguration {

    private String host;
    private String db;
    private String user;
    private String pwd;

}
