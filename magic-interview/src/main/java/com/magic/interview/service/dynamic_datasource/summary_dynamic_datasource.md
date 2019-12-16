## 数据源动态路由
#### 数据源类及自定义注解：【eg: DataSourceType、DataSource】

* 利用ThreadLocal 传递当前请求的数据源
```
@Getter
public class DataSourceType {

    public enum Type {
        //
        FIRST,
        SECOND;
    }

    protected static final ThreadLocal<Type> threadLocal = new ThreadLocal<>();
}


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataSource {

    DataSourceType.Type value();
}

```

#### AOP拦截注解处理： 【eg: AopDataSource】
```
@Aspect
@Slf4j
@Component
public class AopDataSource {

    @Before("@annotation(dataSource)")
    public void dataSourceInfo(DataSource dataSource) {
        DataSourceType.Type value = dataSource.value();
        DataSourceType.threadLocal.set(value);
    }

    @After("@annotation(com.magic.dao.config.DataSource)")
    public void remove() {
        DataSourceType.threadLocal.remove();
    }
}
```

   
#### 路由类 继承 AbstractRoutingDataSource ：【eg：DataSourceRouting】
   * 从ThreadLocal 拿数据源
```
  public class DataSourceRouting extends AbstractRoutingDataSource {
        @Override
        protected Object determineCurrentLookupKey() {
            return DataSourceType.threadLocal.get();
        }
    }

```


#### 设置DataSource设置：【eg: dao-DbConfig】

   1.设置主数据源（@Primary）和从数据源：
```
    @Bean(name = "first")
        @ConfigurationProperties(prefix = "spring.datasource.druid.first")
        @Primary
        public DruidDataSource firstDtaSource() {
            DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
            return dataSource;
        }
    
        @Bean(name = "second")
        @ConfigurationProperties(prefix = "spring.datasource.druid.second")
        public DruidDataSource secondDataSource() {
            DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
            return dataSource;
        }
    

```
   2.设置AbstractRoutingDataSource的路由数据源及：
        
   * 参数用 @Qualifier 表明bean，否则second仍会使用@Primary数据源
```

        @Bean(name = "dynamicDataSource")
        public DataSourceRouting dataSource(@Qualifier("first") DataSource first, @Qualifier("second") DataSource second) {
            DataSourceRouting dataSourceRouting = new DataSourceRouting();

            //将需要路由的数据源进行set
            dataSourceRouting.setTargetDataSources(ImmutableMap.of(DataSourceType.Type.FIRST, first, DataSourceType.Type.SECOND, second));
            //默认数据源
            dataSourceRouting.setDefaultTargetDataSource(first);
            return dataSourceRouting;
        }
```   
   3.SqlSessionFactory：
   
   ```
     @Bean
     public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dynamicDataSource) throws Exception {
            SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
            
            //设为动态数据源，否则请求都会直接走@Primary数据源，起不到路由作用
            factoryBean.setDataSource(dynamicDataSource);
            return factoryBean.getObject();
        }
```

#### Mapper中使用
```
        @Select("select * from record0")
        @ResultType(Record0.class)
        //自定义注解设数据源
        @DataSource(DataSourceType.Type.SECOND)
        List<Record0> getRecord0List();
```  
