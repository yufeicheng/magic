### RateLimiter限流  【eg:RateLimitService】

## nginx 限流

#### ngx_http_limit_conn_module：连接数限流模块

1. 对同一ip，域名进行连接数、并发数限制【只有正在被处理的请求所在的连接才会被计数】。
 
2. 在 conf 的 http{}添加：
 
 ```
    #限制每个用户的并发连接数，取名one; $binary_remote_addr 对应ip；  $server_name zone 对应域名
    limit_conn_zone $binary_remote_addr zone=one:10m;
    
    #配置记录被限流后的日志级别，默认error级别
    limit_conn_log_level error;

    #配置被限流后返回的状态码，默认返回503
    limit_conn_status 503;
```
3.在 server{} 添加
 
 ```
    #限制用户并发连接数为1 ，one 为http{}中定义的名称
    limit_conn one 1;

```

#### ngx_http_limit_req_module： 漏桶算法实现的请求限流模块

1. 对单个ip请求固定频率，每秒固定处理请求数，若请求频率超过限制域配置的值，请求处理会被延迟或丢弃。

2. 在http{}中配置

 ```

    #区域名称为one，大小为10m，平均处理的请求频率不能超过每秒一次。
    limit_req_zone $binary_remote_addr zone=one:10m rate=1r/s;
```

3.在server{}中配置
```

    #设置每个IP桶的数量为5
    limit_req zone=one burst=5;
```

每个ip的请求限制处理为 1个/s，为每个ip缓存5个请求，超过的请求被丢弃