package com.magic.interview.service.try_finally;

import java.util.HashMap;
import java.util.Map;

/**
 * 执行顺序探究
 *
 * @author Cheng Yufei
 * @create 2019-11-04 10:30
 **/
public class TryFinallyService {

    /**
     * 1. finally 语句在try的return语句执行之后，返回之前执行
     *
     * res:
     * try
     * t1
     * finally
     */
    public static String t() {
        try {
            System.out.println("try");
            return t1();
        } catch (Exception e) {

        }finally {
            System.out.println("finally");
        }
        return "";
    }

    private  static String t1() {
        System.out.println("t1");
        return "t1";
    }


//################################################################################################################################################

    /**
     * 2. finally中的return覆盖try中的return
     * @return
     */
    public static int t2() {
        int a = 0;
        try {
            return a += 10;
        } catch (Exception e) {

        }finally {
            return a += 20;
        }
    }

//################################################################################################################################################

    /**
     * 3. 如果finally语句中没有return语句覆盖返回值，那么原来的返回值可能因为finally里的修改而改变也可能不变
     * finally中对a的赋值无效
     * @return
     */
    public static int t3() {
        int a = 0;
        try {
            return a += 10;
        } catch (Exception e) {

        }finally {
            System.out.println(a);
            a =20;
        }
        return 500;
    }

    /**
     * 3. 如果finally语句中没有return语句覆盖返回值，那么原来的返回值可能因为finally里的修改而改变也可能不变
     * finally中对map同一键值的修改有效，但赋值null无效
     *
     * t3 t4 对值的修改影响实际上是java值传递的影响：【Java中只有值传递：调用函数时，传入的是实际参数的副本，对参数的修改不会影响实际参数的值】
     *      a. 实参在栈中，直接拷贝该值，操作不影响实参值；
     *      b。在堆中，拷贝引用地址，但对堆中的操作是可见的
     *
     *
     * @return
     */
    public static Map t4() {
        HashMap<String, String> map = new HashMap<>();
        map.put("AA", "B");
        try {
            map.put("AA", "C");
            return map;
        } catch (Exception e) {

        }finally {
            map.put("AA", "D");
            map = null;
        }
        return map;
    }

    public static void main(String[] args) {
        //System.out.println(t3());
        System.out.println(t4().get("AA"));
    }
}
