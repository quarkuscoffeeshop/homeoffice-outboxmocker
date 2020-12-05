package io.quarkuscoffeeshop.counter.domain.commands;

import io.quarkuscoffeeshop.counter.domain.LineItem;
import io.quarkuscoffeeshop.counter.domain.Location;
import io.quarkuscoffeeshop.counter.domain.OrderSource;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PlaceOrderCommand {

  private final String id;

  private OrderSource orderSource;

  private Location location;

  private String loyaltyMemberId;

  private List<LineItem> baristaLineItems;

  private List<LineItem> kitchenLineItems;

  private final Instant timestamp;

  public PlaceOrderCommand() {
    this.id = UUID.randomUUID().toString();
    this.timestamp = Instant.now();
  }

  public PlaceOrderCommand(OrderSource orderSource, Location location, String loyaltyMemberId, List<LineItem> baristaLineItems, List<LineItem> kitchenLineItems) {
    this.id = UUID.randomUUID().toString();
    this.orderSource = orderSource;
    this.location = location;
    this.loyaltyMemberId = loyaltyMemberId;
    this.baristaLineItems = baristaLineItems;
    this.kitchenLineItems = kitchenLineItems;
    this.timestamp = Instant.now();
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
    return Optional.ofNullable(loyaltyMemberId);
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public void setLoyaltyMemberId(String loyaltyMemberId) {
    this.loyaltyMemberId = loyaltyMemberId;
  }

  public String getId() {
    return id;
  }

  public OrderSource getOrderSource() {
    return orderSource;
  }

  public void setOrderSource(OrderSource orderSource) {
    this.orderSource = orderSource;
  }

  public Instant getTimestamp() {
    return timestamp;
  }
}
