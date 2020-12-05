package io.quarkuscoffeeshop.counter.domain.valueobjects;

import io.quarkuscoffeeshop.counter.domain.OrderStatus;

public class OrderUpdate {

  private final String id;

  private final OrderStatus orderStatus;

  public OrderUpdate(String id, OrderStatus orderStatus) {
    this.id = id;
    this.orderStatus = orderStatus;
  }

  @Override
  public String toString() {
    return "OrderUpdate{" +
      "id='" + id + '\'' +
      ", orderStatus=" + orderStatus +
      '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof OrderUpdate)) return false;

    OrderUpdate that = (OrderUpdate) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    return orderStatus == that.orderStatus;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (orderStatus != null ? orderStatus.hashCode() : 0);
    return result;
  }

  public String getId() {
    return id;
  }

  public OrderStatus getOrderStatus() {
    return orderStatus;
  }
}
