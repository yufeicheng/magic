package com.magic.interview.service.valueandel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Cheng Yufei
 * @create 2021-01-28 2:45 下午
 **/
@RestController
@RequestMapping("/value")
public class ValueController {

    @Autowired
    private ValueService valueService;

    @GetMapping("/list")
    public String get() {
        return valueService.getList();
    }
}
