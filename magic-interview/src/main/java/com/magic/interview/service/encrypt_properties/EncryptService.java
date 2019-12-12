package com.magic.interview.service.encrypt_properties;

import com.google.common.collect.ImmutableMap;
import com.magic.dao.mapper.UserMapper;
import com.magic.dao.model.User;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Cheng Yufei
 * @create 2019-12-03 15:44
 **/
@Service
@Slf4j
public class EncryptService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private BasicTextEncryptor textEncryptor;

    /**
     * 明文加密生成加密串用于配置文件替换
     * @return
     */
    public Map encryProperties() {
        String username = textEncryptor.encrypt("slave");
        String pwd = textEncryptor.encrypt("slave@2019");
        return ImmutableMap.of("username", username, "pwd", pwd);
    }

    public List<User> getUsers() {
        List<User> list = userMapper.getUserList();
        return list;
    }
}
