### Validate

1.自带注解：使用时须在类上添加注解：@Validated

 * @AssertFalse 所注解的元素必须是Boolean类型，且值为false
 * @AssertTrue 所注解的元素必须是Boolean类型，且值为true
 * @DecimalMax 所注解的元素必须是数字，且值小于等于给定的值
 * @DecimalMin 所注解的元素必须是数字，且值大于等于给定的值
 * @Digits 所注解的元素必须是数字，且值必须是指定的位数
 * @Future 所注解的元素必须是将来某个日期
 * @Max 所注解的元素必须是数字，且值小于等于给定的值
 * @Min 所注解的元素必须是数字，且值小于等于给定的值
 * @Range 所注解的元素需在指定范围区间内
 * @NotNull 所注解的元素值不能为null
 * @NotBlank 所注解的元素值有内容
 * @Null 所注解的元素值为null
 * @Past 所注解的元素必须是某个过去的日期
 * @PastOrPresent 所注解的元素必须是过去某个或现在日期
 * @Pattern 所注解的元素必须满足给定的正则表达式
 * @Size 所注解的元素必须是String、集合或数组，且长度大小需保证在给定范围之内
 * @Email 所注解的元素需满足Email格式
 
 
2.自定义校验注解+规则 

1. @MyCheck :
    ```
     1. 自定义注解添加：
           @Constraint(validatedBy = MyCheckValidated.class) //指定校验规则
           @Repeatable(MyCheck.MyChecks.class)  // 可以在同一位置重复添加注解

     2. LombokDto类上加 @MyCheck ，和属性名对应：@MyCheck(startTime = "beginTime",endTime = "overTime",message = "结束时间不能早于开始时间")   
   ```
2. 检验规则：[eg:MyCheckValidated]
    ```
        1. MyCheckValidated implements ConstraintValidator<MyCheck,Object> , 初始方法中指定属性和MyCheck中的对应值 ；校验方法中设置校验规则；
        
        
   ```
3. 使用： [eg:ValidatedController]
    ```
       1. 入参参数 LombokDto 前添加： @Validated
       2. BindingResult 形参处理 自定义校验规则的错误信息，不是用此参数接口错误时返回400
   ```   

4. 配置：[eg:ValidatedConfig]
    ```
        1. 多个校验时，有一个错误就直接报错，不会在继续往下走进行其他的校验，快速失败。
   
        Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory()
                       
   ```   
5. 异常处理：
    ```
        1. ValidateExceptionhandle : 拦截 ValidationException 异常完成对 非实体类参数校验的异常全局处理；
        2. 实体类参数校验异常使用 BindingResult  
   ```
   
6. 分组校验
    ```
        1. 定义空 interface GroupA
        2. LombokDto: cid 属性 @NotNull(groups = GroupA.class) 指定分组，
        3. ValidatedController/ myCheck 方法中 参数前 @Validated(GroupA.class) 指定分组校验才会生效；
        4. 同一个实体类，不同的接口，并不是都需要对cid非空校验，所以指定分组，不需要校验的无需指定分组；
    ```