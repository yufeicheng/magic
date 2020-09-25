package com.magic.interview.service.ordernum;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * 订单号生成：时间[三位毫秒]+机器ip+自增号
 *
 * @author Cheng Yufei
 * @create 2020-09-14 10:54
 **/
@Service
public class OrderGenService {

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

	private static AtomicInteger seq = new AtomicInteger(1000);

	List<String> list = Collections.synchronizedList(new ArrayList<String>());

	/**
	 * jmeter 测试
	 */
	public void generate() {
		list.add(generateNo());
	}

	public void getResult() {
		Set<String> set = new HashSet<>(list);
		System.out.println("生成订单号个数：" + list.size());
		System.out.println("重复订单号个数：" + (list.size() - set.size()));
		Collections.reverse(list);
		list.stream().limit(10).forEach(p -> System.out.println(p));
		if (list.size() != set.size()) {
			ArrayList<String> newList = new ArrayList<>(this.list);
			System.out.println("重复订单号：" + newList.removeAll(set));
		}
	}

	/**
	 * 本地测试
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		List<String> list = Collections.synchronizedList(new ArrayList<String>());
		IntStream.rangeClosed(0, 9000).parallel().forEach(s -> {
			list.add(generateNo());
		});

		Set<String> set = new HashSet<>(list);
		System.out.println("生成订单号：" + list.size());
		System.out.println("重复订单号：" + (list.size() - set.size()));
		list.stream().limit(10).forEach(p -> System.out.println(p));
	}

	public static String generateNo() {

		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));

		if (seq.intValue() > 9990) {
			System.out.println("----reset-----");
			seq.getAndSet(1000);
		}
		return formatter.format(now) + Ip.INSTANCE.ipSuffix + seq.getAndIncrement();
	}

	private enum Ip {

		//
		INSTANCE;
		private String ipSuffix;

		Ip() {
			System.out.println(">>>初始化 ip<<<");
			try {
				ipSuffix = getIp();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}

		private String getIp() throws UnknownHostException {

			InetAddress localHost = InetAddress.getLocalHost();
			String hostAddress = localHost.getHostAddress();

			if (StringUtils.isBlank(hostAddress)) {
				int i = ThreadLocalRandom.current().nextInt(10, 20);
				return String.valueOf(i);
			}

			String str = hostAddress.split("\\.")[3];
			if (str.length() == 2) {
				return str;
			}
			//1位或3位补充位数
			str = "0" + str;
			return str.substring(str.length() - 2);
		}
	}


}
