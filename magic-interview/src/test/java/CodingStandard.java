import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.MonthDay;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 阿里编码规范
 *
 * @author Cheng Yufei
 * @create 2020-04-24 11:10
 **/
public class CodingStandard {

    /**
     * 1.Collectors.toMap 时选择 有 mergeFunction 参数的方法，定义value处理策略，避免主键冲突错误， 返回 {version=V};
     * 2.转map时 value=null 会报NPE,转时候注意非空判断；
     * 3.map. keySet entrySet  values 返回的集合不能进行添加操作【UnsupportedOperationException】
     */
    @Test
    public void toMap() {
        ArrayList<Pair<String, String>> list = new ArrayList<>();
        list.add(Pair.of("version", "A"));
        list.add(Pair.of("version", null));
        list.add(Pair.of("version", "V"));
        list.add(Pair.of("version", "C"));

        Map<String, String> map = list.stream().filter(p -> Objects.nonNull(p.getValue())).collect(Collectors.toMap(Pair::getKey, Pair::getValue, (v1, v2) -> v2));
        System.out.println(map);

    }

    /**
     * 1.List -> array: 使用 toArray(T[]);
     * 2.直接使用toArray ，返回Object[]，强转会：ClassCastException
     * <p>
     * 3。array -> list:  Arrays.asList 返回Arrays内部类ArrayList，并没有实现集合的修改方法，add/remove/clear 【UnsupportedOperationException】
     */
    @Test
    public void arrayList() {

        ArrayList<String> list = Lists.newArrayList("A", "B");
        String[] arr = list.toArray(new String[0]);
        System.out.println(arr.length);

        //不可强转
        //String[] strings = (String[]) list.toArray();

        List<String> strings = Arrays.asList(arr);
        //不可操作
        //strings.add("C");

    }

    /**
     * List.subList 后 返回ArrayList内部类SubList：
     * <p>
     * 1.不可强转为ArrayList【ClassCastException】
     * 2. 原集合list 不能 add 、remove 【ConcurrentModificationException】
     * 3. 子集合的 add 、remove 操作反映到原集合上
     * 4. list.addAll 时，要对传入集合非空判断，addAll时会对传入集合 toArray 处理，所以 NPE
     */
    @Test
    public void subList() {
        ArrayList<Pair<String, String>> list = new ArrayList<>();
        list.add(Pair.of("version", "A"));
        list.add(Pair.of("version2", null));

        List<Pair<String, String>> pairs = list.subList(0, 2);
        //不可强转ArrayList
        //ArrayList<Pair<String, String>> pairs = (ArrayList<Pair<String, String>>) list.subList(0, 2);

        //不可操作原集合
        //list.add(Pair.of("version", "A"));

        //可反映到原集合
        pairs.remove(1);
        pairs.add(Pair.of("version3", "D"));
        System.out.println(pairs);
        System.out.println(list);

        List addLIst = null;
        //list.addAll(addLIst);
    }


    /**
     * 使用BigDecimal对象时采用String参数的构造方式
     */
    @Test
    public void compareFloat() {

        float a = 1.0f - 0.9f;
        float b = 0.9f - 0.8f;
        //false
        System.out.println(a == b);
        //1
        System.out.println(BigDecimal.valueOf(a).compareTo(BigDecimal.valueOf(b)));

        BigDecimal decimal = new BigDecimal("0.9");
        int compare = (new BigDecimal("1.0").subtract(decimal)).compareTo(decimal.subtract(new BigDecimal("0.8")));
        System.out.println(compare);

        System.out.println(Instant.now().toEpochMilli());
        System.out.println(System.currentTimeMillis());

    }

    /**
     * switch 传入String等包装类条件时，非空判断否则 NPE
     */
    @Test
    public void switchString() {
        System.out.println(sw(null));
    }

    private String sw(Integer s) {
        String res;
        switch (s) {
           /* case "A":
                res = "A";
                break;
            case  "B":
                res = "B";
                break;
            default:
                res = "default";
                break;*/
            case 1:
                res = "A";
                break;
            case 2:
                res = "B";
                break;
            default:
                res = "default";
                break;
        }
        return res;
    }


    /**
     * 三目运算符： 注意表达式1 和 表达式2的类型，可能由于自动拆箱导致NPE
     * 类型对齐的自动拆箱操作：
     * 1。表达式1和2中只要有一个是原始类型。
     * 2. 表达式1和2类型不一致时，会强制拆箱升级成范围更大的类型。
     */
    @Test
    public void sanmu() {

        Integer a = 1;
        Integer b = 2;
        Integer c = null;
        boolean flag = false;

        //NPE: a*b为int类型
        int i = flag ? a * b : c;

    }

}
