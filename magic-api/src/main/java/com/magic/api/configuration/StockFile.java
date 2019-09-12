package com.magic.api.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Cheng Yufei
 * @create 2019-08-12 21:54
 **/
@Component
@Profile("dev")
public class StockFile {

    private StockConfiguration stockConfiguration;

    @Autowired
    public void setStockConfiguration(StockConfiguration stockConfiguration) throws IOException {

        StringBuilder builder = new StringBuilder();
        builder.append("db.stock.host=").append(stockConfiguration.getHost()).append(System.lineSeparator());


        /*Resource resource = new ClassPathResource("config/config.properties");
        File file1 = resource.getFile();*/

        //可在启动时候将配置写入config.properties ,但在打包时无法完成【打包使用 maven-resources-plugin 插件】
        String path = ResourceUtils.getFile("classpath:config.properties").getPath();
        Files.write(Paths.get(path), builder.toString().getBytes());

    }
}
