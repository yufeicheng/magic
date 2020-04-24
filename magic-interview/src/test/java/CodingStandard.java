import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
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
     * 1.Collectors.toMap 时选择 有 mergeFunction 参数的方法，避免主键冲突错误， 返回 {version=V};
     * 2.转map时 value=null 会报NPE,转时候注意非空判断
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


        List<Pair<String, String>> pairs = list.subList(0, 2);

        list.add(Pair.of("version", "D"));
        System.out.println(pairs);

    }

    /**
     * List -> array: 使用 toArray(T[])
     */
    @Test
    public void arrayList() {

        ArrayList<String> list = Lists.newArrayList("A", "B");
        String[] arr = list.toArray(new String[0]);

    }

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
}
