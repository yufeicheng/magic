package com.magic.interview.service.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author Cheng Yufei
 * @create 2020-10-19 17:08
 **/

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 1, time = 2, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = 0, time = 1, timeUnit = TimeUnit.SECONDS)
@Threads(2)
@Fork(1)
public class JmhTest {

	public static void main(String[] args) throws RunnerException {
		Options options = new OptionsBuilder().include(JmhTest.class.getSimpleName()).build();
		new Runner(options).run();
	}


	@State(Scope.Thread)
	public static class ThreadState {
		int j = 0;
	}


	@Benchmark
	public Integer test1(ThreadState threadState) throws InterruptedException {
		Thread.sleep(2500);
		System.out.println("----->>>>>>>>>" + Thread.currentThread().getName());
		for (int i = 0; i < 10; i++) {
			try {
				if (i == 10) {
					throw new RuntimeException();
				}
				threadState.j++;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		System.out.println(threadState.j);
		return threadState.j;
	}
}

/**
 * 1.mode:
 * Throughput: 整体吞吐量，例如“1秒内可以执行多少次调用”。
 * AverageTime: 调用的平均时间，例如“每次调用平均耗时xxx毫秒”。
 * SampleTime: 随机取样，最后输出取样结果的分布，例如“99%的调用在xxx毫秒以内，99.99%的调用在xxx毫秒以内”
 * SingleShotTime: 以上模式都是默认一次 iteration 是 1s，唯有 SingleShotTime 是只运行一次。往往同时把 warmup 次数设为0，用于测试冷启动时的性能
 *
 *
 * 2.@State:类注解，JMH测试类必须使用@State注解，State定义了一个类实例的生命周期，可以类比Spring Bean的Scope。由于JMH允许多线程同时执行测试，不同的选项含义如下：
 *
 *		 Scope.Thread：默认的State，每个测试线程分配一个实例；
 * 		Scope.Benchmark：所有测试线程共享一个实例，用于测试有状态实例在多线程共享下的性能；
 * 		Scope.Group：每个线程组共享一个实例；

 * 3.@OutputTimeUnit
 *
 * 　　　　benchmark 结果所使用的时间单位，可用于类或者方法注解，使用java.util.concurrent.TimeUnit中的标准时间单位。
 *
 *
 *
 *4.Iteration
 *
 * 　　Iteration 是 JMH 进行测试的最小单位。在大部分模式下，一次 iteration 代表的是一秒，JMH 会在这一秒内不断调用需要 benchmark 的方法，然后根据模式对其采样，计算吞吐量，计算平均执行时间等。
 *
 * 　　Warmup
 *
 * 　　Warmup 是指在实际进行 benchmark 前先进行预热的行为。为什么需要预热？因为 JVM 的 JIT 机制的存在，如果某个函数被调用多次之后，JVM 会尝试将其编译成为机器码从而提高执行速度。为了让 benchmark 的结果更加接近真实情况就需要进行预热。
 */
