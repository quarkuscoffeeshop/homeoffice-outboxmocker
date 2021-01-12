package io.quarkuscoffeeshop.counter.domain;

import io.quarkuscoffeeshop.counter.domain.commands.PlaceOrderCommand;
import io.quarkuscoffeeshop.counter.domain.events.OrderCreatedEvent;
import io.quarkuscoffeeshop.counter.domain.events.OrderUpdatedEvent;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderEventResult;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderTicket;
import io.quarkuscoffeeshop.counter.domain.valueobjects.TicketUp;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

  Logger logger = LoggerFactory.getLogger(OrderTest.class);

  @Test
  public void testProcessPlaceOrderCommandWithSingleBaristaItem() {
    PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(
      OrderSource.WEB,
      Location.ATLANTA,
      null,
      new ArrayList<>() {{
        add(new LineItem(Item.COFFEE_BLACK, "Paul"));
      }},
      null);

    OrderEventResult orderEventResult = Order.process(placeOrderCommand);
    assertNotNull(orderEventResult, "The OrderEventResult should not be null");
    assertNotNull(orderEventResult.getOrder(), "The OrderEventResult should contain an Order object");
    assertNotNull(orderEventResult.getOutboxEvents(), "The OrderEventResult should contain a Collection of ExportedEvents");
    assertEquals(1, orderEventResult.getOutboxEvents().size(), "There should be only 1 ExportedEvent");
    orderEventResult.getOutboxEvents().forEach(exportedEvent -> logger.info("ExportedEvent {}", exportedEvent.getPayload()));
    assertTrue((orderEventResult.getOutboxEvents().get(0).getClass().equals(OrderCreatedEvent.class)), "The event should be an instance of OrderCreatedEvent");
    assertEquals(placeOrderCommand.getId(), orderEventResult.getOrder().getOrderId(), "The Order's id should match the PlaceOrderCommand's id");

  }

  @Test
  public void testProcessPlaceOrderCommandWithSingleBaristaItemAndSingleKitchenItem() {

    PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(
      OrderSource.WEB,
      Location.CHARLOTTE,
      null,
      new ArrayList<>() {{
        add(new LineItem(Item.COFFEE_BLACK, "Paul"));
      }},
      new ArrayList<>() {{
        add(new LineItem(Item.CAKEPOP, "John"));
      }});

    OrderEventResult orderEventResult = Order.process(placeOrderCommand);
    logger.debug("OrderEventResult {}", orderEventResult.toString());
    assertNotNull(orderEventResult, "The OrderEventResult should not be null");
    assertNotNull(orderEventResult.getOrder(), "The OrderEventResult should contain an Order object");
    assertNotNull(orderEventResult.getOutboxEvents(), "The OrderEventResult should contain a Collection of ExportedEvents");
    assertEquals(1, orderEventResult.getOutboxEvents().size(), "There should be only 1 ExportedEvent");
    orderEventResult.getOutboxEvents().forEach(exportedEvent -> logger.info("ExportedEvent {}", exportedEvent.getPayload()));
    assertTrue((orderEventResult.getOutboxEvents().get(0).getClass().equals(OrderCreatedEvent.class)), "The event should be an instance of OrderCreatedEvent");
    assertEquals(placeOrderCommand.getId(), orderEventResult.getOrder().getOrderId(), "The Order's id should match the PlaceOrderCommand's id");
  }

  @Test
  public void testUpdateOrderAfterOrderUpdatedEvent() {

    String orderId = UUID.randomUUID().toString();
    Order order = new Order(orderId);
    order.setOrderSource(OrderSource.WEB);
    order.setTimestamp(Instant.now());
    LineItem lineItem = new LineItem(Item.COFFEE_BLACK, "Lemmy", order);
    order.addBaristaLineItem(lineItem);

    TicketUp ticketUp = new TicketUp(order.getOrderId(), lineItem.getItemId(), lineItem.getItem(), lineItem.getName(), "baristaNme");

    OrderEventResult orderEventResult = order.applyOrderTicketUp(ticketUp);
    assertNotNull(orderEventResult, "The OrderEventResult should not be null");
    assertNotNull(orderEventResult.getOrder(), "The OrderEventResult should contain an Order object");
    assertEquals(order.getOrderId(), orderEventResult.getOrder().getOrderId(), "The Order's id should match the Order's id");
    assertNotNull(orderEventResult.getOutboxEvents(), "The OrderEventResult should contain a Collection of ExportedEvents");
    assertEquals(1, orderEventResult.getOutboxEvents().size(), "There should be only 1 ExportedEvent");
    assertTrue((orderEventResult.getOutboxEvents().get(0).getClass().equals(OrderUpdatedEvent.class)), "The event should be an instance of OrderUpdatedEvent");
    assertEquals(LineItemStatus.FULFILLED, orderEventResult.getOrder().getBaristaLineItems().get().get(0).getLineItemStatus());

  }

}
