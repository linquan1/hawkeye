package com.quan.hawkeye.test;

import com.quan.hawkeye.domain.SysMetrics;
import com.quan.hawkeye.publisher.MetricsEvent;
import com.lmax.disruptor.RingBuffer;


public class MetricsEventProducer {
    public final RingBuffer<MetricsEvent> ringBuffer;

    public MetricsEventProducer(RingBuffer<MetricsEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(SysMetrics metrics) {
        long sequence = ringBuffer.next();
        MetricsEvent metricsEvent = ringBuffer.get(sequence);
        metricsEvent.setValue(metrics);
        ringBuffer.publish(sequence);
    }
}
