package io.quarkuscoffeeshop.counter.valueobjects;

import io.quarkuscoffeeshop.counter.domain.Item;
import io.quarkuscoffeeshop.counter.domain.Order;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderTicket;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderUpdate;

import java.util.UUID;

public class ValueObjectMocker {

    public static OrderTicket orderTicketBlackCoffee() {

        return new OrderTicket(UUID.randomUUID().toString(), UUID.randomUUID().toString(), Item.COFFEE_BLACK, "Lemmy");
    }

}
