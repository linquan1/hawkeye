package com.quan.hawkeye.publisher;

public class MetricsEvent<T> {
    private T value;

    private Integer eventType;

    public MetricsEvent() {
    }

    public MetricsEvent(T value, Integer eventType) {
        this.value = value;
        this.eventType = eventType;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
