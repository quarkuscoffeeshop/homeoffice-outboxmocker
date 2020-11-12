package io.quarkuscoffeeshop.counter.domain;

import io.debezium.outbox.quarkus.ExportedEvent;

import java.util.ArrayList;
import java.util.List;

public class OrderEvent {

    private Order order;

    private List<ExportedEvent> events;

    public Order getOrder() {
        return order;
    }

    public void addEvent(final ExportedEvent event) {
        if (this.events == null) {
            this.events = new ArrayList<>();
        }
        this.events.add(event);
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<ExportedEvent> getEvents() {
        return events;
    }

    public void setEvents(List<ExportedEvent> events) {
        this.events = events;
    }
}
