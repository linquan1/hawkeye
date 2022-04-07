package com.quan.hawkeye.publisher;

import com.quan.hawkeye.util.SimpleLogger;
import com.lmax.disruptor.RingBuffer;

import java.util.concurrent.CopyOnWriteArrayList;

public class MetricsEventPublisher implements EventPublisher{

    private static SimpleLogger logger = SimpleLogger.getLogger(MetricsEventPublisher.class);

    public final CopyOnWriteArrayList<RingBuffer<MetricsEvent>> buffers = new CopyOnWriteArrayList<>();

    private final static MetricsEventPublisher publisher = new MetricsEventPublisher();

    private MetricsEventPublisher() {}

    public static MetricsEventPublisher getPublisher() {
        return publisher;
    }


    @Override
    public void publish(MetricsEvent event) {

        if (buffers.isEmpty()) {
            logger.warn("no subscriber find, ignore.");
            return;
        }

        for (RingBuffer<MetricsEvent> ringBuffer : buffers) {
            long sequence = ringBuffer.next();
            MetricsEvent metricsEvent = ringBuffer.get(sequence);
            metricsEvent.setEventType(event.getEventType());
            metricsEvent.setValue(event.getValue());
            ringBuffer.publish(sequence);
        }
    }

    @Override
    public void register(RingBuffer<MetricsEvent> buffer) {
        buffers.add(buffer);
    }

}
