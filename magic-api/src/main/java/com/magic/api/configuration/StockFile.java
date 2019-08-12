package com.magic.api.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Cheng Yufei
 * @create 2019-08-12 21:54
 **/
@Component
public class StockFile {

    private StockConfiguration stockConfiguration;

    @Autowired
    public void setStockConfiguration(StockConfiguration stockConfiguration) throws IOException {

        StringBuilder builder = new StringBuilder();
        builder.append("db.stock.host=").append(stockConfiguration.getHost()).append(System.lineSeparator());

        File file = ResourceUtils.getFile("classpath:config/config.properties");

        Resource resource = new ClassPathResource("config/config.properties");
        File file1 = resource.getFile();

        //Files.write(Paths.get("",))
        //Files.write(builder.toString().getBytes(),file1);

       /* File file3= ResourceUtils.getFile("classpath:config");
        System.out.println();*/


    }
}
