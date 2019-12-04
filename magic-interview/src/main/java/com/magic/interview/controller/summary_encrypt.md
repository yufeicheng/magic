###配置文件加密

1. 引用：

    ```
            <dependency>
               <groupId>com.github.ulisesbocchio</groupId>
               <artifactId>jasypt-spring-boot-starter</artifactId>
               <version>2.1.2</version>
           </dependency>
   ```

2. 对明文加密得到加密串：
    
     ```
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
                encryptor.setPassword("e2TxKdz");
                String username = encryptor.encrypt("slave");
                String pwd = encryptor.encrypt("slave@2019"); 
   
   ```
3. application-dao-dev.yml 替换加密串：

    ```
        spring.data.druid.username: ENC(+dqSK3T2OBdy0mGXRh0ZMQ==)
        spring.data.druid.password: ENC(HWuWGiatmGzF41wYyrsF482iEVWGrfmf)
   ```   
   
4. 指定加密盐值三种方式：

    ```
        1. 在application.yml中配置：
                jasypt:
                  encryptor:
                    password: e2TxKdz
   
        2. 启动脚本中指定： -Djasypt.encryptor.password=e2TxKdz
   
        3. 盐值 放在本地文件中：
   
            启动类处理：【MagicInterviewApplication】
                @PropertySource(value = {"file:D:/encrypt.properties"})
   
                @Value("${jasypt.encryptor.password}")
   
                @Bean 创建 BasicTextEncryptor 类，并 setPassword
    
   ```   