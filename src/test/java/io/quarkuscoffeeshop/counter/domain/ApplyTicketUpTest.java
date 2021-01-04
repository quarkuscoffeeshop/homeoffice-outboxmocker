package io.quarkuscoffeeshop.counter.domain;


import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderEventResult;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderTicket;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ApplyTicketUpTest {

    @Test
    public void testApplyTicket() {

        //create an Order
        Order order = new Order(
                UUID.randomUUID().toString(),
                OrderSource.COUNTER,
                null,
                Instant.now(),
                OrderStatus.IN_PROGRESS,
                null,
                null
        );
        order.addBaristaLineItem(new LineItem(Item.CAPPUCCINO, "Huey"));

        OrderTicket orderTicket = new OrderTicket(
                order.getOrderId(),
                order.getBaristaLineItems().get().get(0).getItemId(),
                order.getBaristaLineItems().get().get(0).getItem(),
                order.getBaristaLineItems().get().get(0).getName());

        OrderEventResult orderEventResult = order.applyOrderTicketUp(orderTicket);

        assertNotNull(orderEventResult);
        Order resultingOrder = orderEventResult.getOrder();
        assertEquals(order.getOrderId(), resultingOrder.getOrderId());
        LineItem resultingLineItem = resultingOrder.getBaristaLineItems().get().get(0);
        assertEquals(LineItemStatus.FULFILLED, resultingLineItem.getLineItemStatus());
    }
}
