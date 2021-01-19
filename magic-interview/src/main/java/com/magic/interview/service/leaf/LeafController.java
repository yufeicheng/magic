package com.magic.interview.service.leaf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Cheng Yufei
 * @create 2019-11-05 11:06
 **/
@RestController
@RequestMapping("/leaf")
@Slf4j
public class LeafController {

    @Autowired
    private LeafService leafService;

    /**
     * 直接使用 ArrayList 多线程 add时会有 ArrayIndexOutOfBoundsException 错误
     */
    private static List<Integer> list  = Collections.synchronizedList(new ArrayList<Integer>());
    private static HashSet<Integer> set = new HashSet<>();

    @GetMapping("/segment")
    public String segment(@RequestParam String key) {

        IntStream.rangeClosed(1, 100).parallel().forEach(k -> {
            String id = leafService.generator(key);
            log.info(">>获取ID：{}", id);
            list.add(Integer.valueOf(id));
        });
        set.addAll(list);
        log.info(">>生成订单号：{} 个", list.size());
        log.info(">>重复订单号：{} 个", list.size() - set.size());
        return "success";
    }

}