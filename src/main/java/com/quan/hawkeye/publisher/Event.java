package com.quan.hawkeye.publisher;

public class Event<T> {
    private Integer eventType;

    private T data;

    public Event(Integer eventType, T data) {
        this.eventType = eventType;
        this.data = data;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
