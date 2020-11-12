package io.quarkuscoffeeshop.counter.domain;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrderTest {

    Logger logger = LoggerFactory.getLogger(OrderTest.class);

    @Test
    public void testProcessPlaceOrderCommandWithSingleBaristaItem() {
        PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(UUID.randomUUID().toString(),
                "WEB",
                new ArrayList<LineItem>() {{
                    add(new LineItem(Item.COFFEE_BLACK, "Paul"));
                }},
                null);
    }

    @Test
    public void testProcessPlaceOrderCommandWithSingleBaristaItemAndSingleKitchenItem() {

        PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(UUID.randomUUID().toString(),
                "WEB",
                new ArrayList<LineItem>() {{
                    add(new LineItem(Item.COFFEE_BLACK, "Paul"));
                }},
                new ArrayList<LineItem>() {{
                    add(new LineItem(Item.CAKEPOP, "John"));
                }});

        OrderEvent result = Order.process(placeOrderCommand);
        System.out.println(result.toString());
        assertNotNull(result);
        assertNotNull(result.getOrder());
        assertNotNull(result.getEvents());
        result.getEvents().forEach(exportedEvent -> {
            System.out.println(exportedEvent.getPayload());
        });
        assertEquals(1, result.getEvents().size());
        assertEquals(placeOrderCommand.getId(), result.getOrder().getOrderId());
    }

}
