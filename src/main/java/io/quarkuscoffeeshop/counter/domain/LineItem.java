package io.quarkuscoffeeshop.counter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "LineItems")
public class LineItem extends PanacheEntityBase {

  @JsonIgnore
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "orderId", nullable = false)
  Order order;

  @Id
  @GeneratedValue(strategy="org.hibernate.id.Assigned")
  @Column(nullable = false, name = "id")
  private final String id;

  @Enumerated(EnumType.STRING)
  private Item item;

  private String name;

  private BigDecimal price;

  @Enumerated(EnumType.STRING)
  private LineItemStatus lineItemStatus;

  public LineItem() {
    this.id = UUID.randomUUID().toString();
  }

  public LineItem(Item item, String name) {
    this.id = UUID.randomUUID().toString();
    this.item = item;
    this.name = name;
  }

  public LineItem(Item item, String name, Order order) {
    this.id = UUID.randomUUID().toString();
    this.item = item;
    this.name = name;
    this.lineItemStatus = LineItemStatus.PLACED;
    this.order = order;
  }

  public LineItem(Item item, String name, LineItemStatus lineItemStatus, Order order) {
    this.id = UUID.randomUUID().toString();
    this.item = item;
    this.name = name;
    this.lineItemStatus = lineItemStatus;
    this.order = order;
  }

  public BigDecimal getPrice() {
    return this.item.getPrice();
  }

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LineItemStatus getLineItemStatus() {
    return lineItemStatus;
  }

  public void setLineItemStatus(LineItemStatus lineItemStatus) {
    this.lineItemStatus = lineItemStatus;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public String getId() {
    return this.id;
  }
}
