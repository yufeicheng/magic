package com.magic.interview.service.use_custom_starter;

import cn.anony.boot.core.SmsSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 测试自定义starter
 *
 * @author Cheng Yufei
 * @create 2019-11-11 17:05
 **/

@Service
@Slf4j
public class UseStarterService {

    @Autowired
    private SmsSender smsSender;

    public String use() {
        return smsSender.send();
    }
}
