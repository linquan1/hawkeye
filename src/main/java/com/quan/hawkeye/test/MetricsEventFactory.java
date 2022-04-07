package com.quan.hawkeye.test;

import com.quan.hawkeye.publisher.MetricsEvent;
import com.lmax.disruptor.EventFactory;

public class MetricsEventFactory implements EventFactory<MetricsEvent> {
    public MetricsEvent newInstance() {
        return new MetricsEvent();
    }
}
