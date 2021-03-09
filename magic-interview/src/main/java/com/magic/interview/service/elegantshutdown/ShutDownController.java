package com.magic.interview.service.elegantshutdown;

import com.magic.dao.mapper.UserMapper;
import com.magic.dao.model.User;
import com.magic.dao.model.UserExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author Cheng Yufei
 * @create 2021-03-08 2:58 下午
 **/
@RestController
@Slf4j
@RequestMapping("/shutDown")
public class ShutDownController implements ApplicationContextAware {

    private ApplicationContext context;

    @Autowired
    private UserMapper userMapper;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @PostMapping("/shutdown")
    public void shutdown() {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) this.context;
        configurableApplicationContext.close();
    }

    @GetMapping("/info")
    public List info()  {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<User> users = userMapper.selectByExample(new UserExample());
        log.info(">>>>info:{}",users.toString());
        return users;
    }

}
