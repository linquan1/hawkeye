package com.quan.hawkeye.test;

import com.quan.hawkeye.domain.SysMetrics;
import com.quan.hawkeye.publisher.MetricsEvent;
import com.quan.hawkeye.reportor.MetricsReportor;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DisruptorMain {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        // 2.创建工厂
        EventFactory<MetricsEvent> eventFactory = new MetricsEventFactory();
        // 3.创建ringBuffer 大小
        int ringBufferSize = 2; // ringBufferSize大小一定要是2的N次方
        // 4.创建Disruptor
        Disruptor<MetricsEvent> disruptor = new Disruptor<MetricsEvent>(eventFactory, ringBufferSize, executor,
                ProducerType.SINGLE, new SleepingWaitStrategy());
        // 5.连接消费端方法
        disruptor.handleEventsWith(new MetricsReportor());
        // 6.启动
        RingBuffer<MetricsEvent> ringBuffer = disruptor.start();
        // 7.创建RingBuffer容器
//        RingBuffer<MetricsEvent> ringBuffer = disruptor.getRingBuffer();


        // 8.创建生产者
        MetricsEventProducer producer = new MetricsEventProducer(ringBuffer);

        SysMetrics baseResourceMetrics = new SysMetrics();
//        baseResourceMetrics.setCpu("1");
//        baseResourceMetrics.setDisk("2");
//        baseResourceMetrics.setMemory("3");
        producer.onData(baseResourceMetrics);


        System.out.println("执行结束");
        //10.关闭disruptor和executor
        disruptor.shutdown();
        executor.shutdown();

    }
}
