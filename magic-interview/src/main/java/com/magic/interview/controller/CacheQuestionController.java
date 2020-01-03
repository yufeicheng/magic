package com.magic.interview.controller;

import com.magic.interview.service.cache_question.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cheng Yufei
 * @create 2019-11-05 11:06
 **/
@RestController
@RequestMapping("/ques")
@Validated
@Slf4j
public class CacheQuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/breakdown")
    public Object breakdown() throws InterruptedException {
        return questionService.breakdown();
    }

    @GetMapping("/redis/{type}")
    public String set(@PathVariable String type, @RequestParam String key, @RequestParam(required = false) String value) {
        return questionService.set(type,key, value);
    }
}