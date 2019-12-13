### 数据源动态路由
1. 设置DataSource设置：【eg: dao-DbConfig】

   1. 设置主数据源（@Primary）和从数据源：
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

   2. 设置动态数据源：
```
        @Bean(name = "dynamicDataSource")
        public DataSourceRouting dataSource(@Qualifier("first") DataSource first, @Qualifier("second") DataSource second) {
            DataSourceRouting dataSourceRouting = new DataSourceRouting();
            dataSourceRouting.setTargetDataSources(ImmutableMap.of(DataSourceType.Type.FIRST, first, DataSourceType.Type.SECOND, second));
            dataSourceRouting.setDefaultTargetDataSource(first);
            return dataSourceRouting;
        }
```   
