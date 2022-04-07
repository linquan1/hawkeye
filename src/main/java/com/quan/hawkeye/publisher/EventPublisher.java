package com.quan.hawkeye.publisher;

import com.lmax.disruptor.RingBuffer;

public interface EventPublisher {

    void publish(MetricsEvent event);

    void register(RingBuffer<MetricsEvent> buffer);
}
