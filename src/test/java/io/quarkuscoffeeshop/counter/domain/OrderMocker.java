package io.quarkuscoffeeshop.counter.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

public class OrderMocker {

    public static Order mockOrder() {
        //  public Order(String orderId, OrderSource orderSource, String loyaltyMemberId, Instant timestamp, OrderStatus orderStatus, List<LineItem> baristaLineItems, List<LineItem> kitchenLineItems) {
        return new Order(
                UUID.randomUUID().toString(),
                OrderSource.WEB,
                null,
                Instant.now(),
                OrderStatus.IN_PROGRESS,
                new ArrayList<>() {{
                    add(new LineItem(Item.COFFEE_BLACK, "Bruno"));
                }},
                null);

    }
}
