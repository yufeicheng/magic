package com.magic.interview.service.http_client;

import com.alibaba.fastjson.JSONObject;
import com.magic.dao.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author Cheng Yufei
 * @create 2020-01-15 10:08
 **/
@Service
@Slf4j
public class HttpClientService {

    private CloseableHttpClient client;

    @PostConstruct
    public void init() {
         client = HttpClientBuilder.create().build();
    }

    public void post() throws IOException {

        String url = "";
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json;charset=utf8");

        User user = new User(1, "taylor", "");
        post.setEntity(new StringEntity(JSONObject.toJSONString(user), Charset.forName("utf-8")));
        CloseableHttpResponse response = client.execute(post);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    /**
     * 文件上传
     * @throws IOException
     */
    public void upload() throws IOException {
        String url = "";
        HttpPost post = new HttpPost(url);

        File file = new File("D:/test/xxx");
        FileBody fileBody = new FileBody(file);

        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        entityBuilder.addPart("file", fileBody);

        post.setEntity(entityBuilder.build());

        CloseableHttpResponse response = client.execute(post);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }
}
