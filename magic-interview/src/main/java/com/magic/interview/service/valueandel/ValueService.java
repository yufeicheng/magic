package com.magic.interview.service.valueandel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Cheng Yufei
 * @create 2021-01-28 2:40 下午
 **/
@Service
@Slf4j
public class ValueService {

    @Value("#{'${test.list:}'.split(',')}")
    private List<String> splitList;

    @Value("#{'${test.list:}'.empty ? null : '${test.list:}'.split(',')}")
    private List<String> whetherNullSplitList;

    @Value("#{'${test.list:}'}")
    //@Value("${test.list}")
    private List<String> list;

    @Value("#{${test.map}}")
    private Map<String, Object> map;

    public String getList() {
        list.stream().forEach(System.out::println);
        System.out.println("list.size: " + list.size());

        splitList.stream().forEach(System.out::println);
        System.out.println("splitList.size: " + splitList.size());

        System.out.println(whetherNullSplitList);

        System.out.println(map);
        return list.toString();
    }
}


/**
 * A: yml 中配置的List key【元素逗号分割形式】不存在时：
 *     1。@Value("${test.list}") \ @Value("#{'${test.list}'}") 两者启动均报错。
 *
 *     2。@Value("#{'${test.list:}'}")，：提供默认值，启动不报错，size=0。
 *
 *     3。@Value("#{'${test.list:}'.split(',')}")，：提供默认值，启动不报错，包含一个空串，size=1。
 *
 *  B: yml  中配置的List【元素以 - 分割形式】，需以类，属性List 来加载，耦合性高，建议不采用。
 *
 *  C: yml 中配置 Map： '{"key": value, "key": "zhangsan"}'
 *      1。@Value("#{${test.map}}")
 *
 */
