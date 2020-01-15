### 事件发布监听机制

1. 添加事件 implements **_ApplicationEvent_** 【eg：RegisterEvent】
    
    1. 重写构造器，添加需要的成员变量
    
    ```
        public class RegisterEvent extends ApplicationEvent {
        
            @Getter
            private User user;
           
            public RegisterEvent(Object source, User user) {
                super(source);
                this.user = user;
            }
        }
   ```
   
2. 事件发布 【eg：BusinessService】   

    1. 在业务类中引入 ApplicationContext类后进行发布
    
    ```
   
        applicationContext.publishEvent(new RegisterEvent(this, user));
   ```
   
3. 事件监听

    1. @EventListener 实现: 【eg：RegisterDbListener、RegisterMsgListener】
    
        1. 类添加@Component，方法传入自定义的事件类。
        
        2. 此时是请求线程同步执行，@Async 改为异步
    
    ```
   
        @Component
        @Slf4j
        public class RegisterMsgListener {
        
            @EventListener
            //@Async
            public void handler(RegisterEvent event) {
                User user = event.getUser();
        
                //短信处理
                log.info(">>>监听注册事件，短信 处理成功");
            }
        }
   ```
   
   2. implements ApplicationListener 实现
   
   3. 前两种方式多个监听器之间都是无序执行的，implements SmartApplicationListener来实现监听器之间的顺序执行。【eg：OrderRegisterDbListener、OrderRegisterMsgListener】
   
   ```
   
    @Component
    @Slf4j
    public class OrderRegisterDbListener implements SmartApplicationListener {
    
        @Autowired
        private UserMapper userMapper;
    
        /**
         * 接受到的监听事件类型的匹配
         * @param eventType
         * @return
         */
        @Override
        public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
            return eventType.isAssignableFrom(RegisterEvent.class);
        }
    
        /**
         * 事件发布者类匹配
         * @param sourceType
         * @return
         */
        @Override
        public boolean supportsSourceType(Class<?> sourceType) {
            return sourceType.isAssignableFrom(BusinessService.class);
        }
    
        /**
         * 监听执行顺序
         * @return
         */
        @Override
        public int getOrder() {
            return 1;
        }
    
        /**
         *  supportsEventType && supportsSourceType 时才会执行
         * @param event
         */
        @Override
        public void onApplicationEvent(ApplicationEvent event) {
            RegisterEvent registerEvent = (RegisterEvent) event;
            User user = registerEvent.getUser();
    
            userMapper.insertSelective(user);
            log.info(">>>监听注册事件，DB 处理成功");
        }
    }
   ```