package io.quarkuscoffeeshop.counter.domain;

import io.quarkuscoffeeshop.counter.domain.commands.PlaceOrderCommand;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderEventResult;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderTicket;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    OrderEventResult result = Order.process(placeOrderCommand);
    System.out.println(result.toString());
    assertNotNull(result);
    assertNotNull(result.getOrder());
    assertNotNull(result.getOutboxEvents());
    result.getOutboxEvents().forEach(exportedEvent -> System.out.println(exportedEvent.getPayload()));
    assertEquals(1, result.getOutboxEvents().size());
    assertEquals(placeOrderCommand.getId(), result.getOrder().getOrderId());
  }

  @Test
  public void testUpdateOrderAfterOrderUpdatedEvent() {

    String orderId = UUID.randomUUID().toString();
    Order order = new Order(orderId);
    order.setOrderSource(OrderSource.WEB);
    order.setTimestamp(Instant.now());
    LineItem lineItem = new LineItem(Item.COFFEE_BLACK, "Lemmy", order);
    order.addBaristaLineItem(lineItem);

    OrderTicket orderTicket = new OrderTicket(order.getOrderId(), lineItem.getItemId(), lineItem.getItem(), lineItem.getName());

    OrderEventResult orderEventResult = order.applyOrderTicketUp(orderTicket);
    assertNotNull(orderEventResult);
    assertNotNull(orderEventResult.getOrder());
    assertNotNull(orderEventResult.getOutboxEvents());
    assertEquals(1, orderEventResult.getOutboxEvents().size());
    assertEquals(orderId, orderEventResult.getOrder().getOrderId());
    assertEquals(LineItemStatus.FULFILLED, orderEventResult.getOrder().getBaristaLineItems().get().get(0).getLineItemStatus());

  }

}
