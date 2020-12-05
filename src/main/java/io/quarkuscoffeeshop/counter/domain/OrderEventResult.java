package io.quarkuscoffeeshop.counter.domain;

import io.debezium.outbox.quarkus.ExportedEvent;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderUpdate;

import java.util.ArrayList;
import java.util.List;

/**
 * Value object returned from an Order.  Contains the Order aggregate and a List ExportedEvent
 */
public class OrderEventResult {

    private Order order;

    private List<ExportedEvent> outboxEvents;

    private List<OrderUpdate> orderUpdates;

    public Order getOrder() {
        return order;
    }

    public void addEvent(final ExportedEvent event) {
        if (this.outboxEvents == null) {
            this.outboxEvents = new ArrayList<>();
        }
        this.outboxEvents.add(event);
    }

  public void addUpdate(final OrderUpdate orderUpdate) {
    if (this.orderUpdates == null) {
      this.orderUpdates = new ArrayList<>();
    }
    this.orderUpdates.add(orderUpdate);
  }

  @Override
  public String toString() {
    return "OrderEventResult{" +
      "order=" + order +
      ", outboxEvents=" + outboxEvents +
      ", orderUpdates=" + orderUpdates +
      '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof OrderEventResult)) return false;

    OrderEventResult that = (OrderEventResult) o;

    if (getOrder() != null ? !getOrder().equals(that.getOrder()) : that.getOrder() != null) return false;
    if (getOutboxEvents() != null ? !getOutboxEvents().equals(that.getOutboxEvents()) : that.getOutboxEvents() != null)
      return false;
    return getOrderUpdates() != null ? getOrderUpdates().equals(that.getOrderUpdates()) : that.getOrderUpdates() == null;
  }

  @Override
  public int hashCode() {
    int result = getOrder() != null ? getOrder().hashCode() : 0;
    result = 31 * result + (getOutboxEvents() != null ? getOutboxEvents().hashCode() : 0);
    result = 31 * result + (getOrderUpdates() != null ? getOrderUpdates().hashCode() : 0);
    return result;
  }

  public void setOrder(Order order) {
        this.order = order;
    }

    public List<ExportedEvent> getOutboxEvents() {
        return outboxEvents;
    }

    public void setOutboxEvents(List<ExportedEvent> outboxEvents) {
        this.outboxEvents = outboxEvents;
    }

  public List<OrderUpdate> getOrderUpdates() {
    return orderUpdates;
  }

  public void setOrderUpdates(List<OrderUpdate> orderUpdates) {
    this.orderUpdates = orderUpdates;
  }
}
