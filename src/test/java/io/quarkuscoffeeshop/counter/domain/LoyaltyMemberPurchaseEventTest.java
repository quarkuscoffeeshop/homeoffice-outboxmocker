package io.quarkuscoffeeshop.counter.domain;

import io.quarkuscoffeeshop.counter.domain.commands.PlaceOrderCommand;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderEventResult;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoyaltyMemberPurchaseEventTest {


    @Test
    public void testLoyaltyMemberPurchaseEvent() {

        PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(
                UUID.randomUUID().toString(),
                OrderSource.COUNTER,
                Location.RALEIGH,
                "FawningFalcon",
                Optional.of(new ArrayList() {{
                    add(new LineItem(Item.COFFEE_BLACK, "Paul"));
                }}),
                null
        );

        OrderEventResult orderEventResult = Order.process(placeOrderCommand);
        assertEquals(2, orderEventResult.getOutboxEvents().size());
        assertTrue(orderEventResult.getOutboxEvents().stream().anyMatch(event -> {
            return event.getType().equals("LoyaltyMemberPurchaseEvent");
        }));
    }
}
