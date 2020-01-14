### 自定义方法中参数注解

1. 示例用于获取登录用户

2. 注解@CurrentUser

    ```
    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = ElementType.PARAMETER)
    public @interface CurrentUser {
    }
   ```
3. 方法参数注解处理类 【普通java类 eg：CurrentUserHandler】   

    ```
   
   public class CurrentUserHandler implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(CurrentUser.class) && User.class.isAssignableFrom(methodParameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String sessionId = request.getHeader("sessionId");

        /*WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
        UserMapper userMapper = context.getBean(UserMapper.class);*/

        UserMapper userMapper = MyApplicationContext.getBean(UserMapper.class);
        User user = userMapper.selectById(Integer.valueOf(sessionId));
        //User user = userMapper.selectByPrimaryKey(Integer.valueOf(sessionId));
        return user;
    }
   ```
    1. 实现 HandlerMethodArgumentResolver
    
    2. supportsParameter()检验是否包含@CurrentUser注解及注解类型是否是User，返回true才会执行resolveArgument() 方法。
    
    3. resolveArgument() 方法：
    
        1. 从 request中获取id，userMapper查询返回用户
        
        2. 普通类获取spring 容器中bean 方法：
        
            第一种：
            ```
                WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
           
                获取ApplicationContext，进而 getBean 获取。
                  
           ```
            第二种：【eg: MyApplicationContext】
              
              实现 ApplicationContextAware
              
              ```
                @Component
                public class MyApplicationContext implements ApplicationContextAware {
                
                    private static ApplicationContext context;
                
                    public static  <T> T getBean(Class<T> requiredType) {
                        return context.getBean(requiredType);
                    }
                
                    /**
                     * 工程启动时执行此方法
                     * @param applicationContext
                     * @throws BeansException
                     */
                    @Override
                    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
                        MyApplicationContext.context = applicationContext;
                    }
                }
           
           ```
              
4. 将注解处理类加入WebMvcConfig 【eg: MvcConfig】    

    ```
   @Configuration
   public class MvcConfig implements WebMvcConfigurer {
   
       @Override
       public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
           resolvers.add(new CurrentUserHandler());
       }
   }
   
   ```    
   
5. 使用 【eg：ParameterAnnController】         

    ```
   
   @GetMapping("/anno")
       public List<User> getUsers(@CurrentUser Record0 user){
   
   }
   ```
    
   
插曲：
 
 application-dao.yml 中使用 Mybatis.mapper-locations 来指定 sql的xml，但在执行时，会找不到xml对应的sql，但是能使用 @Select 注解型sql 。
 
 所以在 DbConfig 中代码指定xml位置：
 
 ```

 @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dynamicDataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        factoryBean.setMapperLocations(resolver.getResources("classpath*:mapper/*.xml"));

        factoryBean.setDataSource(dynamicDataSource);
        return factoryBean.getObject();
    }

``` 