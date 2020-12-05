package io.quarkuscoffeeshop.counter.domain.valueobjects;

import io.quarkuscoffeeshop.counter.domain.OrderStatus;

public class OrderUpdate {

  private final String orderId;

  private final String itemId;

  private final OrderStatus orderStatus;

  public OrderUpdate(final String orderId, final String itemId, final OrderStatus orderStatus) {
    this.orderId = orderId;
    this.itemId = itemId;
    this.orderStatus = orderStatus;
  }

  @Override
  public String toString() {
    return "OrderUpdate{" +
      "orderId='" + orderId + '\'' +
      ", itemId='" + itemId + '\'' +
      ", orderStatus=" + orderStatus +
      '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof OrderUpdate)) return false;

    OrderUpdate that = (OrderUpdate) o;

    if (orderId != null ? !orderId.equals(that.orderId) : that.orderId != null) return false;
    if (itemId != null ? !itemId.equals(that.itemId) : that.itemId != null) return false;
    return getOrderStatus() == that.getOrderStatus();
  }

  @Override
  public int hashCode() {
    int result = orderId != null ? orderId.hashCode() : 0;
    result = 31 * result + (itemId != null ? itemId.hashCode() : 0);
    result = 31 * result + (getOrderStatus() != null ? getOrderStatus().hashCode() : 0);
    return result;
  }

  public String getOrderId() {
    return orderId;
  }

  public String getItemId() {
    return itemId;
  }

  public OrderStatus getOrderStatus() {
    return orderStatus;
  }
}
