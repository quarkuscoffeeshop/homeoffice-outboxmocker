package io.quarkuscoffeeshop.counter.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkuscoffeeshop.counter.domain.events.LoyaltyMemberPurchaseEvent;
import io.quarkuscoffeeshop.counter.domain.events.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity @Table(name = "Orders")
public class Order extends PanacheEntityBase {

    @Transient
    static Logger logger = LoggerFactory.getLogger(Order.class);

    @Id
    @Column(nullable = false, name = "orderId")
    private String orderId;

    private String orderSource;

    private String loyaltyMemberId;

    private Instant timestamp;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order", cascade = CascadeType.ALL)
    private List<LineItem> baristaLineItems;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order", cascade = CascadeType.ALL)
    private List<LineItem> kitchenLineItems;

    public static OrderEvent process(final PlaceOrderCommand placeOrderCommand){

        OrderEvent orderEvent = new OrderEvent();

        // build the order from the PlaceOrderCommand
        Order order = new Order();
        order.setOrderId(placeOrderCommand.getId());
        order.setOrderSource(placeOrderCommand.getOrderSource());
        order.setTimestamp(placeOrderCommand.getTimestamp());

        if (placeOrderCommand.getBaristaLineItems().isPresent()) {
            logger.debug("createOrderFromCommand adding beverages {}", placeOrderCommand.getBaristaLineItems().get().size());
            placeOrderCommand.getBaristaLineItems().get().forEach(v -> {
                logger.debug("createOrderFromCommand adding baristaItem from {}", v.toString());
                order.addBaristaLineItem(new LineItem(v.getItem(), v.getName(), order));
            });
        }
        if (placeOrderCommand.getKitchenLineItems().isPresent()) {
            logger.debug("createOrderFromCommand adding kitchenOrders {}", placeOrderCommand.getKitchenLineItems().get().size());
            placeOrderCommand.getKitchenLineItems().get().forEach(v ->{
                logger.debug("createOrderFromCommand adding kitchenItem from {}", v.toString());
                order.addKitchenLineItem(new LineItem(v.getItem(), v.getName(), order));
            });
        }

        orderEvent.setOrder(order);
        orderEvent.addEvent(OrderCreatedEvent.of(order));

        // if this order was places by a Loyalty Member add the appropriate event
        if (placeOrderCommand.getLoyaltyMemberId().isPresent()) {
            order.setLoyaltyMemberId(placeOrderCommand.getLoyaltyMemberId().get());
            orderEvent.addEvent(LoyaltyMemberPurchaseEvent.of(order));
        }

        return orderEvent;
    }

    /**
     * Convenience method to prevent Null Pointer Exceptions
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

    public Optional<List<LineItem>> getKitchenLineItems() {
        return Optional.ofNullable(kitchenLineItems);
    }

    public Optional<String> getLoyaltyMemberId() {
        return Optional.ofNullable(this.loyaltyMemberId);
    }

    public void setLoyaltyMemberId(String loyaltyMemberId) {
        this.loyaltyMemberId = loyaltyMemberId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public void setBaristaLineItems(List<LineItem> baristaLineItems) {
        this.baristaLineItems = baristaLineItems;
    }

    public void setKitchenLineItems(List<LineItem> kitchenLineItems) {
        this.kitchenLineItems = kitchenLineItems;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
