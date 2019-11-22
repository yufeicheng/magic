import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkPositionIndex;


import com.google.common.collect.Lists;
import com.magic.interview.service.validated.LombokDto;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.functors.AnyPredicate;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;

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
        Collections.swap(list_1,0,2);

        //所有元素都替换为指定元素
        Collections.fill(list_1,"P");

        int frequency = Collections.frequency(list_1, "B");

        Collections.sort(list_1, Comparator.naturalOrder());

        //反转
        Collections.reverse(list_1);

        //洗牌
        Collections.shuffle(list_1);

    }


}
