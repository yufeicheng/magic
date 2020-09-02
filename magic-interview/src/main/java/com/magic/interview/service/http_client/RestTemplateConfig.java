package com.magic.interview.service.http_client;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * @author Cheng Yufei
 * @create 2020-09-01 10:11
 **/
@Configuration
public class RestTemplateConfig {

	@Bean(name="restTemplate")
	public RestTemplate init() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

		RequestConfig requestConfig = RequestConfig.custom()
				//等待数据超时时间
				.setSocketTimeout(6000)
				//连接到目标url的超时时间
				.setConnectTimeout(6000)
				//从池中获取连接超时时间，如果在超时时间内，没有可用连接，就会抛出ConnectionPoolTimeoutException异常
				.setConnectionRequestTimeout(1000)
				.build();

		//长连接保持30s。超过TTL值的持久连接将不会被重用，默认2000ms
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(30,TimeUnit.MILLISECONDS);
		//默认为每个给定路由创建不超过2个并发连接，并且总共不超过20个连接
		//连接池最大连接数
		connectionManager.setMaxTotal(500);
		//每个路由的最大连接数
		connectionManager.setDefaultMaxPerRoute(100);
		//connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("https://www.baidu.com")),50);

		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig)
				.evictExpiredConnections()
				//自定义连接存活策略
				//.setKeepAliveStrategy()
				//忽略SSL
				//.setSSLSocketFactory(new SSLConnectionSocketFactory(new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(), new NoopHostnameVerifier()))
				//设置重试，默认3次重试
				.setRetryHandler(new DefaultHttpRequestRetryHandler(0,false))
				.build();

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
		//监听器:可在请求前设置Header中的 Bear等权限参数
		restTemplate.getInterceptors().add(new RestTemplateInterpect());
		return restTemplate;

		//return new RestTemplateBuilder().build();
		//return new RestTemplate();
	}

	/**
	 * 1.new RestTemplate(): 使用SimpleClientHttpRequestFactory,java.net.URLConnection作为client客户端。
	 *
	 * 2.new RestTemplateBuilder().build():使用 HttpComponentsClientHttpRequestsFactory,以 apache httpclient作为客户端
	 *
	 * 3.evictExpiredConnections:
	 * 			服务端关闭连接，发送 FIN 包,在这个 FIN 包发送但是还未到达客户端期间，客户端如果继续复用这个 TCP 连接发送 HTTP 请求报文的话，服务端会因为在四次挥手期间不接收报文而发送 RST 报文给客户端，客户端收到 RST 报文就会提示异常 				(即 NoHttpResponseException)。
	 * 			后台线程驱逐过期连接。
	 *
	 * 4.PoolingHttpClientConnectionManager：有连接池，HttpClient就可以同时执行多个线程的请求
	 */
}
