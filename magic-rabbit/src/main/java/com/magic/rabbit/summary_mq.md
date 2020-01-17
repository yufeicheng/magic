## 消息投递与消费的准确性

### 手工确认模式、投递/路由确认模式开启【application.yml】

    ```
        listener:
              simple:
                prefetch: 100
                #手动确认
                acknowledge-mode: manual
            #开启消息投递->Exchange的回调
            publisher-confirms: true
            #开启Exchange->Queue的回调
            publisher-returns: true
    ```

### RabbitTemplate 、 Exchange等配置【eg：MqConfig】 

   1. RabbitTemplate：
    
        1. 消息转换设置为：Jackson2JsonMessageConverter，能在@RabbitListener方法中接受自定义的消息组装类（CommonMessage）。
        
        2. 设置ConfirmCallback 【消息生产者 -> Exchange】的回调；
           设置 ReturnCallback 【Exchange -> Queue】 的回调。
           
          ```
                @Bean
                    public RabbitTemplate initRabbitTemplate(@Qualifier("messageConverter") MessageConverter converter) {
                        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
                        rabbitTemplate.setMessageConverter(converter);
                
                        //消息是否成功发送到Exchange
                        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
                            if (ack) {
                                log.info(">>>消息成功发送到Exchange");
                                String msgId = correlationData.getId();
                                //更新消息状态
                                MsgLog msgLog = new MsgLog();
                                msgLog.setMsgId(msgId);
                                //投递成功
                                msgLog.setStatus(MsgLogStatus.DELIVER_SUCCESS.getCode());
                                msgLogMapper.updateByPrimaryKeySelective(msgLog);
                            } else {
                                log.info("消息发送到Exchange失败, {}, cause: {}", correlationData, cause);
                            }
                        });
                
                        // 触发setReturnCallback回调必须设置mandatory=true, 否则Exchange没有找到Queue就会丢弃掉消息, 而不会触发回调
                        rabbitTemplate.setMandatory(true);
                
                        // 消息是否从Exchange路由到Queue, 注意: 这是一个失败回调, 只有消息从Exchange路由到Queue失败才会回调这个方法
                        rabbitTemplate.setReturnCallback((message, replyCode, replyText,
                                                          exchange, routingKey) -> {
                
                            log.info("消息从Exchange路由到Queue失败: exchange: {}, route: {}, replyCode: {}, replyText: {}, message: {}", exchange, routingKey, replyCode, replyText, message);
                        });
                        return rabbitTemplate;
                    }
                    
        ```
      
     
   2. Exchange\Queue\Binding 设置

        ```
            @Autowired
                public void init(AmqpAdmin amqpAdmin) {
                    Exchange common_exchange = ExchangeBuilder.directExchange(Constant.COMMON_EXCHANGE).durable(true).build();
            
                    Queue common_queue = QueueBuilder.durable(Constant.COMMON_QUEUE).build();
            
                    Binding binding = BindingBuilder.bind(common_queue).to(common_exchange).with(Constant.COMMON_ROUTING_KEY).noargs();
            
                    amqpAdmin.declareExchange(common_exchange);
                    amqpAdmin.declareQueue(common_queue);
                    amqpAdmin.declareBinding(binding);
            
                }    
        ```
      
### 消息生产投递 【eg：PublishMsgService】

 1. 设置消息组装类信息，消息入库，投递
 
    ```
        public String send() {
                String msgId = UUID.randomUUID().toString();
        
                CommonMessage commonMessage = new CommonMessage();
                commonMessage.setMsgId(msgId);
        
                User user = new User();
                user.setUsername("green");
                commonMessage.setData(user);
        
                MsgLog msgLog = new MsgLog();
                msgLog.setMsgId(msgId);
                msgLog.setMsg(JSONObject.toJSONString(commonMessage));
                msgLog.setExchange(Constant.COMMON_EXCHANGE);
                msgLog.setRoutingKey(Constant.COMMON_ROUTING_KEY);
                msgLog.setNextTryTime(Date.from(LocalDateTime.now().plus(2, ChronoUnit.MINUTES).atZone(ZoneId.systemDefault()).toInstant()));
                msgLogMapper.insertSelective(msgLog);
        
                rabbitTemplate.convertAndSend(Constant.COMMON_EXCHANGE, Constant.COMMON_ROUTING_KEY, commonMessage, new CorrelationData(msgId));
        
                return "success";
            }
    ```
    
### 消息消费 【eg：ConsumeMsg】

   ```
    @RabbitListener(queues = {Constant.COMMON_QUEUE})
        public void consume(CommonMessage commonMessage, Message message, Channel channel) throws Exception {
    
            log.info(">>>接受消息:{}", commonMessage.toString());
    
    
            //业务处理
            Thread.sleep(200);
            //throw new Exception();
        }
   ``` 

### AOP拦截消费者，进行幂等、消息状态更新、消息确认操作 【eg：AckAopHandler】

   ```
    @Around(value = "@annotation(rabbitListener)")
        public Object handler(ProceedingJoinPoint pjp, RabbitListener rabbitListener) throws IOException {
    
            Object proceed = new Object();
            Message message = null;
            Channel channel = null;
            try {
                Object[] args = pjp.getArgs();
                CommonMessage commonMessage = (CommonMessage) args[0];
                message = (Message) args[1];
                channel = (Channel) args[2];
    
                //幂等
                //eg：未ack消息，再次启动时会重新被消费，但此消息之前已消费，此处保证不在次消费
                String msgId = commonMessage.getMsgId();
                MsgLog msgLog = msgLogMapper.selectByPrimaryKey(msgId);
                if (Objects.isNull(msgLog) || (Objects.nonNull(msgLog) && msgLog.getStatus().equals(MsgLogStatus.SPEND.getCode()))) {
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                    log.info(">>重复消费");
                    return proceed;
                }
    
                proceed = pjp.proceed();
    
                MsgLog updateMsg = new MsgLog();
                updateMsg.setMsgId((commonMessage).getMsgId());
                updateMsg.setStatus(MsgLogStatus.SPEND.getCode());
                msgLogMapper.updateByPrimaryKeySelective(updateMsg);
                //不手动确认时，即使消息消费完成，仍会保留在mq中，直至确认；会在服务重启时再次进行投递，此时需要保证幂等性
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                log.info(">>消息已确认，消费成功");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                //业务异常的消息重新入队消费
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
            return proceed;
        }

   ```

### 异常情况：

1. 投递 Exchange 失败时，ConfirmCallback 中有打印失败信息；【Exchange不存在时】
    
    ps：ConfirmCallback：正确投递和失败都会走这个回调。

2. 路由Queue 失败，ReturnCallback 中打印失败信息；【queue不存在】

    ps：ReturnCallback 只有路由queue失败时才会走这个回调。
    
3. 未进行channel.basicAck 的消息实际上时走完消费流程的，但仍会存在mq中，直到下次服务启动会重新消费，所以此处要保证幂等性。

4. 业务异常，消息进入 channel.basicNack ,将消息重新入队，会不停的进行消费，直至确认。

5. 由于网络或者mq宕机，使得Confirm未执行，此时消息一直处于投递中状态。定时拉取投递中的消息在符合下次投递时间和最大投递次数的情况下，进行再次投递。而消费端需要保证消息多次消费的幂等性。    