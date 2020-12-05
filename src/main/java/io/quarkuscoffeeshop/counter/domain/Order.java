package io.quarkuscoffeeshop.counter.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkuscoffeeshop.counter.domain.commands.PlaceOrderCommand;
import io.quarkuscoffeeshop.counter.domain.events.LoyaltyMemberPurchaseEvent;
import io.quarkuscoffeeshop.counter.domain.events.OrderCreatedEvent;
import io.quarkuscoffeeshop.counter.domain.events.OrderUpdatedEvent;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderEventResult;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderTicket;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.sound.sampled.Line;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

@Entity
@Table(name = "Orders")
public class Order extends PanacheEntityBase {

  @Transient
  static Logger logger = LoggerFactory.getLogger(Order.class);

  @Id
  @Column(nullable = false, unique = true, name = "order_id")
  private String orderId;

  private OrderSource orderSource;

  private String loyaltyMemberId;

  private Instant timestamp;

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "order", cascade = CascadeType.ALL)
  private List<LineItem> baristaLineItems;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "order", cascade = CascadeType.ALL)
  private List<LineItem> kitchenLineItems;

  /**
   * Updates the lineItem corresponding to the ticket, creates the appropriate domain events,
   * creates value objects to notify the system, checks the order to see if all items are completed,
   * and updates the order if necessary
   *
   * All corresponding objects are returned in an OrderEventResult
   *
   * @param orderTicket
   * @return OrderEventResult
   */
  public OrderEventResult applyOrderTicketUp(final OrderTicket orderTicket) {

    // set the LineItem's new status
    markFulfilled(orderTicket.getOrderId());

    // create the domain event
    OrderUpdatedEvent orderUpdatedEvent = OrderUpdatedEvent.of(this);

    // create the update value object
    OrderUpdate orderUpdate = new OrderUpdate(orderTicket.getOrderId(), orderTicket.getLineItemId(), OrderStatus.FULFILLED);

    // check the status of the Order itself and update if necessary
    if(Stream.concat(this.baristaLineItems.stream(), this.kitchenLineItems.stream())
      .allMatch(lineItem -> {
        return lineItem.getLineItemStatus().equals(LineItemStatus.FULFILLED);
      })){
      this.orderStatus = OrderStatus.FULFILLED;
    };

    // return the results
    OrderEventResult orderEventResult = new OrderEventResult();
    orderEventResult.setOrder(this);
    orderEventResult.addEvent(orderUpdatedEvent);
    return orderEventResult;
  }

  private void markFulfilled(final String orderId) {
    if (getBaristaLineItems().isPresent()) {
       getBaristaLineItems().get().stream().forEach(lineItem -> {
         if(orderId.equals(lineItem.getItemId())){
           lineItem.setLineItemStatus(LineItemStatus.FULFILLED);
         }
       });
    }
    if (getKitchenLineItems().isPresent()) {
      getBaristaLineItems().get().stream().forEach(lineItem -> {
        if(orderId.equals(lineItem.getItemId())){
          lineItem.setLineItemStatus(LineItemStatus.FULFILLED);
        }
      });
    }
  }

  /**
   * Creates and returns a new OrderEventResult containing the Order aggregate built from the PlaceOrderCommand
   * and an OrderCreatedEvent
   *
   * @param placeOrderCommand PlaceOrderCommand
   * @return OrderEventResult
   */
  public static OrderEventResult process(final PlaceOrderCommand placeOrderCommand) {

    OrderEventResult orderEventResult = new OrderEventResult();

    // build the order from the PlaceOrderCommand
    Order order = new Order();
    order.setOrderSource(placeOrderCommand.getOrderSource());
    order.setTimestamp(placeOrderCommand.getTimestamp());

    if (placeOrderCommand.getBaristaLineItems().isPresent()) {
      logger.debug("createOrderFromCommand adding beverages {}", placeOrderCommand.getBaristaLineItems().get().size());
      placeOrderCommand.getBaristaLineItems().get().forEach(v -> {
        logger.debug("createOrderFromCommand adding baristaItem from {}", v.toString());
        LineItem lineItem = new LineItem(v.getItem(), v.getName(), order);
        order.addBaristaLineItem(lineItem);
        orderEventResult.addBaristaTicket(new OrderTicket(order.getOrderId(), lineItem.getItemId(), lineItem.getItem(), lineItem.getName()));
      });
    }
    if (placeOrderCommand.getKitchenLineItems().isPresent()) {
      logger.debug("createOrderFromCommand adding kitchenOrders {}", placeOrderCommand.getKitchenLineItems().get().size());
      placeOrderCommand.getKitchenLineItems().get().forEach(v -> {
        logger.debug("createOrderFromCommand adding kitchenItem from {}", v.toString());
        LineItem lineItem = new LineItem(v.getItem(), v.getName(), order);
        order.addKitchenLineItem(lineItem);
        orderEventResult.addKitchenTicket(new OrderTicket(order.getOrderId(), lineItem.getItemId(), lineItem.getItem(), lineItem.getName()));
      });
    }

    orderEventResult.setOrder(order);
    orderEventResult.addEvent(OrderCreatedEvent.of(order));

    // if this order was places by a Loyalty Member add the appropriate event
    if (placeOrderCommand.getLoyaltyMemberId().isPresent()) {
      order.setLoyaltyMemberId(placeOrderCommand.getLoyaltyMemberId().get());
      orderEventResult.addEvent(LoyaltyMemberPurchaseEvent.of(order));
    }

    return orderEventResult;
  }

  /**
   * Convenience method to prevent Null Pointer Exceptions
   *
   * @param lineItem
   */
  public void addBaristaLineItem(final LineItem lineItem) {
    if (this.baristaLineItems == null) {
      this.baristaLineItems = new ArrayList<>();
    }
    this.baristaLineItems.add(lineItem);
  }

  /**
   * Convenience method to prevent Null Pointer Exceptions
   *
   * @param lineItem
   */
  public void addKitchenLineItem(final LineItem lineItem) {
    if (this.kitchenLineItems == null) {
      this.kitchenLineItems = new ArrayList<>();
    }
    this.kitchenLineItems.add(lineItem);
  }

  public Optional<List<LineItem>> getBaristaLineItems() {
    return Optional.ofNullable(baristaLineItems);
  }

  public void setBaristaLineItems(List<LineItem> baristaLineItems) {
    this.baristaLineItems = baristaLineItems;
  }

  public Optional<List<LineItem>> getKitchenLineItems() {
    return Optional.ofNullable(kitchenLineItems);
  }

  public void setKitchenLineItems(List<LineItem> kitchenLineItems) {
    this.kitchenLineItems = kitchenLineItems;
  }

  public Optional<String> getLoyaltyMemberId() {
    return Optional.ofNullable(this.loyaltyMemberId);
  }

  public void setLoyaltyMemberId(String loyaltyMemberId) {
    this.loyaltyMemberId = loyaltyMemberId;
  }

  public Order() {
    this.orderId = UUID.randomUUID().toString();
  }

  public Order(String orderId, OrderSource orderSource, String loyaltyMemberId, Instant timestamp, OrderStatus orderStatus, List<LineItem> baristaLineItems, List<LineItem> kitchenLineItems) {
    this.orderId = UUID.randomUUID().toString();
    this.orderSource = orderSource;
    this.loyaltyMemberId = loyaltyMemberId;
    this.timestamp = timestamp;
    this.orderStatus = orderStatus;
    this.baristaLineItems = baristaLineItems;
    this.kitchenLineItems = kitchenLineItems;
  }

  @Override
  public String toString() {
    return "Order{" +
      "orderId='" + orderId + '\'' +
      ", orderSource=" + orderSource +
      ", loyaltyMemberId='" + loyaltyMemberId + '\'' +
      ", timestamp=" + timestamp +
      ", orderStatus=" + orderStatus +
      ", baristaLineItems=" + baristaLineItems +
      ", kitchenLineItems=" + kitchenLineItems +
      '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Order)) return false;

    Order order = (Order) o;

    if (getOrderId() != null ? !getOrderId().equals(order.getOrderId()) : order.getOrderId() != null) return false;
    if (getOrderSource() != order.getOrderSource()) return false;
    if (getLoyaltyMemberId() != null ? !getLoyaltyMemberId().equals(order.getLoyaltyMemberId()) : order.getLoyaltyMemberId() != null)
      return false;
    if (getTimestamp() != null ? !getTimestamp().equals(order.getTimestamp()) : order.getTimestamp() != null)
      return false;
    if (getOrderStatus() != order.getOrderStatus()) return false;
    if (getBaristaLineItems() != null ? !getBaristaLineItems().equals(order.getBaristaLineItems()) : order.getBaristaLineItems() != null)
      return false;
    return getKitchenLineItems() != null ? getKitchenLineItems().equals(order.getKitchenLineItems()) : order.getKitchenLineItems() == null;
  }

  @Override
  public int hashCode() {
    int result = getOrderId() != null ? getOrderId().hashCode() : 0;
    result = 31 * result + (getOrderSource() != null ? getOrderSource().hashCode() : 0);
    result = 31 * result + (getLoyaltyMemberId() != null ? getLoyaltyMemberId().hashCode() : 0);
    result = 31 * result + (getTimestamp() != null ? getTimestamp().hashCode() : 0);
    result = 31 * result + (getOrderStatus() != null ? getOrderStatus().hashCode() : 0);
    result = 31 * result + (getBaristaLineItems() != null ? getBaristaLineItems().hashCode() : 0);
    result = 31 * result + (getKitchenLineItems() != null ? getKitchenLineItems().hashCode() : 0);
    return result;
  }

  public String getOrderId() {
    return orderId;
  }

  public OrderSource getOrderSource() {
    return orderSource;
  }

  public void setOrderSource(OrderSource orderSource) {
    this.orderSource = orderSource;
  }

  public OrderStatus getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(OrderStatus orderStatus) {
    this.orderStatus = orderStatus;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Instant timestamp) {
    this.timestamp = timestamp;
  }
}
