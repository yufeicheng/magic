import com.google.common.collect.Lists;
import com.magic.interview.service.validated.LombokDto;
import jodd.template.StringTemplateParser;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.functors.AnyPredicate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkPositionIndex;

/**
 * @author Cheng Yufei
 * @create 2019-11-04 14:20
 **/
public class TestC {

    /**
     * Preconditions: 静态导入方法
     */
    @Test
    public void checkException() {
        String uid = "aa", pwd = " v";
        //抛出java.lang.NullPointerException
        String s = checkNotNull(uid);

        checkArgument(!StringUtils.isAnyBlank(uid, pwd), "用户名或密码为空！");

        ArrayList<String> strings = Lists.newArrayList("a", "b", "c");
        //[0,size)
        checkElementIndex(3, strings.size());
        //[0,size]
        checkPositionIndex(4, strings.size());

    }

    /**
     * BeanWrapper
     */
    @Test
    public void beanwapper() {

        LombokDto dto = new LombokDto();
        dto.setCid(1);
        dto.setName("北京");
        dto.setTime(new Date());

        Field[] fields = LombokDto.class.getDeclaredFields();

        BeanWrapper wrapper = new BeanWrapperImpl(dto);
        Stream.of(fields).forEach(f -> System.out.println(wrapper.getPropertyValue(f.getName())));
    }

    /**
     * 集合工具类
     */
    @Test
    public void collection() {

        ArrayList<String> list_1 = Lists.newArrayList("A", "B", "C", "D", "B");
        ArrayList<String> list_2 = Lists.newArrayList("C", "D", "e", "f", "H");

        //-------------------------------CollectionUtils-------------------------------

        //交集 [C, D]
        List<String> retainList = ((List<String>) CollectionUtils.intersection(list_1, list_2));
        System.out.println(retainList);

        //并集 [A, B, B, C, D, e, f, H]
        Collection<String> unionList = CollectionUtils.union(list_1, list_2);
        System.out.println(unionList);

        //差集(前者-后者): [A, B, B]---[e, f, H]
        Collection<String> subtractList = CollectionUtils.subtract(list_1, list_2);
        Collection<String> subtractList_2 = CollectionUtils.subtract(list_2, list_1);
        System.out.println(subtractList + "---" + subtractList_2);

        //两个集合的对称差异：[A, B, B, e, f, H]
        Collection<String> disjunction = CollectionUtils.disjunction(list_1, list_2);
        System.out.println(disjunction);

        //按条件取集合: [A]
        Collection<String> selectList = CollectionUtils.select(list_1, AnyPredicate.anyPredicate(e -> e.equals("A")));
        System.out.println(selectList);

        //两者元素是否完全相等: false
        boolean equalCollection = CollectionUtils.isEqualCollection(list_1, list_2);
        System.out.println(equalCollection);

        //元素出现次数：key：集合元素 value：次数  {A=1, B=2, C=1, D=1}
        Map<String, Integer> cardinalityMap = CollectionUtils.getCardinalityMap(list_1);
        System.out.println(cardinalityMap);

        //-------------------------------Collections-------------------------------

        //没有共同之处返回则true ： false
        boolean disjoint = Collections.disjoint(list_1, list_2);
        System.out.println(disjoint);


        String max = Collections.min(list_1);


        //交换位置
        Collections.swap(list_1, 0, 2);

        //所有元素都替换为指定元素
        Collections.fill(list_1, "P");

        int frequency = Collections.frequency(list_1, "B");

        Collections.sort(list_1, Comparator.naturalOrder());

        //反转
        Collections.reverse(list_1);

        //洗牌
        Collections.shuffle(list_1);

    }

    /**
     * IO
     */
    @Test
    public void io() throws IOException {

        //流中读取到List中
        List<String> readLines = IOUtils.readLines(new FileInputStream(new File("D:/新建文本文档.txt")), "utf-8");
        System.out.println(readLines);

        //文件读到List中
        List<String> strings = FileUtils.readLines(new File("D:/新建文本文档.txt"), "utf-8");

        //string 写到文件中
        FileUtils.writeStringToFile(new File("D:/xx.txt"), "写数据到文件中", "utf-8");

    }

    @Test
    public void pairAndTriple() {
        ImmutablePair<String, String> pair = ImmutablePair.of("A", "a");
        System.out.println(pair.left + "----" + pair.right);

        ImmutableTriple<String, String, String> triple = ImmutableTriple.of("A", "a", "c");
        System.out.println(triple.left + "---" + triple.middle + "---" + triple.right);
    }

    @Test
    public void encrypt() {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword("e2TxKdz");

        String res = textEncryptor.decrypt("HWuWGiatmGzF41wYyrsF482iEVWGrfmf");
        System.out.println(res);
    }

    @Test
    public void streamCreate() {
        //iterate: 第一个参数初始值，第二个参数操作
        Stream.iterate(10, n -> n + 1).limit(5).forEach(v -> System.out.println(v));

        Stream.generate(() -> ThreadLocalRandom.current().nextInt(10)).limit(5).forEach(v -> System.out.println(v));

        List<Double> collect = Stream.of(1.2, 2.3, 3.4).collect(Collectors.toList());
        List<Double> collect1 = DoubleStream.of(1.2, 2.3).boxed().collect(Collectors.toList());
        System.out.println(collect + "---" + collect1);
    }

    @Test
    public void concatStream() {

        //流拼接
        Stream<Integer> stream = Stream.of(1, 2, 3);
        Stream<Integer> stream2 = Stream.of(4, 5, 6);
        Stream<Integer> stream3 = Stream.of(7, 8, 9);

      /*  List<Integer> collect = Stream.concat(stream, stream2).collect(Collectors.toList());
        System.out.println(collect);*/

        List<Integer> collect1 = Stream.of(stream, stream2, stream3).flatMap(Function.identity()).collect(Collectors.toList());
        System.out.println(collect1);

    }

    @Test
    public void list() {
        ArrayList<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        System.out.println(list);
        Integer a = 1;
        //int a = 1;
        //包装类删除时，查找的是对象 而不是下标
        list.remove(a);
        System.out.println(list);

    }


    @Test
    public void html() throws IOException {

        String uri = "/Users/chengyufei/Downloads/project/self/Gitee/magic/magic-interview/src/test/java/top.html";

       /* List<String> list = Files.readAllLines(Paths.get(uri), Charset.forName("utf-8"));
        String htmls =list.toString();
        System.out.println(htmls);*/

        StringBuilder builder = new StringBuilder();
        Stream<String> lines = Files.lines(Paths.get(uri));
        lines.forEach(s -> builder.append(s));

        String htmls = builder.toString();

        HashMap<String, String> map = new HashMap<>();
        map.put("name", "榜单");
        map.put("city", "北京");

        StringTemplateParser templateParser = new StringTemplateParser();
        String parse = templateParser.parse(htmls, str -> map.get(str));
        System.out.println(parse);




    }

}
