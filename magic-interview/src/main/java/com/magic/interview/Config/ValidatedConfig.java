package com.magic.interview.Config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


/**
 * @author Cheng Yufei
 * @create 2019-11-06 14:39
 **/
@Configuration
public class ValidatedConfig {

    /**
     * 有多个校验时，有一个校验失败就无需继续往下校验了
     * @return
     */
    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure().failFast(true).buildValidatorFactory();

        return validatorFactory.getValidator();
    }
}
