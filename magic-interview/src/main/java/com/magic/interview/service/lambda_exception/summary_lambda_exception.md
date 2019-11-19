###lambda处理受检查异常

1. lambda中使用try / cache 不美观

2. 包装Function：

    a. [eg: CheckFunction]
    
        ```
          1. @FunctionalInterface : 函数接口
                 * 有且只有一个抽象方法；
                 
                 * 可以包含静态方法、default方法；
                 
                 * 声明覆盖Object中的方法不算抽象方法；
                 
                 * 可以使用一个lambda表达式 表示该接口的实现；
          
          2. 定义抽象方法 : 
                
                R apply(T t) throws Exception;
           
        ```
        
    b. [eg:Handle 接口]
    
      ```
         1. 定义 static 方法，接受CheckFunction参数，try / cache 处理后 ，返回Function，完成对Function的包装       

   ```
           
           
3. 使用： [eg:LambdaExceptionService] 

    ```
        1. 将有受检查异常的方法调用用Handle类包装处理：
        
            .flatMap(Handle.handle(p -> Files.lines(p, Charset.forName("utf-8"))))
   ```           