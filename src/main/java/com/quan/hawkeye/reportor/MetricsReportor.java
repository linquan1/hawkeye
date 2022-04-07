package com.quan.hawkeye.reportor;

import com.quan.hawkeye.publisher.MetricsEvent;
import com.lmax.disruptor.EventHandler;

public class MetricsReportor implements EventHandler<MetricsEvent> {
    public void onEvent(MetricsEvent metricsEvent, long l, boolean b) throws Exception {
        System.out.println("消费事件:" + metricsEvent.getValue().toString());
        Thread.sleep(1000000);
    }
}
