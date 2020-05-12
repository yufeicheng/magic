package com.magic.interview.service.time_geek.error_100;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Cheng Yufei
 * @create 2020-05-12 15:42
 **/
public class Concurrent {

    private static volatile boolean flag = true;

    /**
     * 大量写的场景（10 万次 add 操作），CopyOnWriteArray 几乎比同步的 ArrayList 慢一百倍；
     * 大量读的场景下（100 万次 get 操作），CopyOnWriteArray 又比同步的 ArrayList 快五倍以上
     *
     *
     *
     * 需存储110个元素，已存10个。要求20个线程并发存储剩余100个的元素，根据size计算当前需要补充的元素
     *
     * 使用了线程安全的并发工具，并不代表解决了所有线程安全问题,ConcurrentHashMap 只能保证提供的原子性读写操作是线程安全的。
     *
     * 每个线程的计算size、putAll 仍需 加锁
     *
     * @throws InterruptedException
     */
    @Test
    public void supplement() throws InterruptedException {

        int total = 110;
        ConcurrentHashMap<String, Integer> concurrentHashMap = IntStream.rangeClosed(1, 10).boxed().collect(Collectors.toConcurrentMap(i -> UUID.randomUUID().toString(), Function.identity(), (o1, o2) -> o1, ConcurrentHashMap::new));

        ForkJoinPool forkJoinPool = new ForkJoinPool(20);
        forkJoinPool.submit(() -> {

            //ForkJoinPool设置的并行度影响 parallel开启并行后的执行力
            IntStream.rangeClosed(1, 20).boxed().parallel().forEach(j -> {
                int supplementCount = total - concurrentHashMap.size();
                System.out.println(Thread.currentThread().getName() + j + "：>>需要补充的个数--" + +supplementCount);

                concurrentHashMap.putAll((Map<? extends String, ? extends Integer>) IntStream.rangeClosed(1, supplementCount).boxed().collect(Collectors.toConcurrentMap(i -> UUID.randomUUID().toString(), Function.identity(), (o1, o2) -> o1, ConcurrentHashMap::new)));
            });



            /*Stream.iterate(1, i -> ++i).limit(10).parallel().forEach(j -> {
                int size = concurrentHashMap.size();
                System.out.println(Thread.currentThread().getName() + ">>当前" + j + "：" + size);

                concurrentHashMap.putAll((Map<? extends String, ? extends Integer>) IntStream.rangeClosed(1, total - size).boxed().collect(Collectors.toConcurrentMap(i -> UUID.randomUUID().toString(), Function.identity(), (o1, o2) -> o1, ConcurrentHashMap::new)));
            });*/

        });

        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);

       /* CountDownLatch countDownLatch = new CountDownLatch(10);

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                synchronized (concurrentHashMap) {
                    int supplementCount = total - concurrentHashMap.size();
                    System.out.println(Thread.currentThread().getName() + ">>需要补充的个数：" + supplementCount);

                    concurrentHashMap.putAll((Map<? extends String, ? extends Integer>) IntStream.rangeClosed(1, supplementCount).boxed().collect(Collectors.toConcurrentMap(j -> UUID.randomUUID().toString(), Function.identity(), (o1, o2) -> o1, ConcurrentHashMap::new)));
                }
                countDownLatch.countDown();
            }).start();

        }
        countDownLatch.await();*/

        System.out.println(">>>>finish:" + concurrentHashMap.size());
    }


    /**
     * computeIfAbsent: 多线程统计key频率，线程安全；value 接受Function；key存在时返回旧值；key不存在时存储后返回新值；
     *
     * putIfAbsent： key存在时返回旧值；key不存在时返回null；
     *
     * @throws InterruptedException
     */
    @Test
    public void frequency() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(50);

        ConcurrentHashMap<Integer, LongAdder> keyFrequency = new ConcurrentHashMap<>();

        for (int i = 0; i < 50; i++) {

            new Thread(() -> {
                keyFrequency.computeIfAbsent(ThreadLocalRandom.current().nextInt(5), s -> new LongAdder()).increment();
                //keyFrequency.putIfAbsent(ThreadLocalRandom.current().nextInt(5), new LongAdder()).increment();
                countDownLatch.countDown();
            }).start();

        }
        countDownLatch.await();

        keyFrequency.forEach((k, v) -> {
            System.out.println(k + "->" + v.longValue());
        });

    }


    public static void main(String[] args) {

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            flag = false;
        }).start();

        while (flag) {
            System.out.println("循环。。。");
        }
        System.out.println(">done");
    }
}
