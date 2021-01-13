package io.quarkuscoffeeshop.counter.valueobjects;

import io.quarkuscoffeeshop.counter.domain.Item;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderTicket;
import io.quarkuscoffeeshop.counter.domain.valueobjects.TicketUp;

import java.util.UUID;

public class ValueObjectMocker {

    public static OrderTicket orderTicketBlackCoffee() {

        return new OrderTicket(UUID.randomUUID().toString(), UUID.randomUUID().toString(), Item.COFFEE_BLACK, "Lemmy");
    }

    public static TicketUp ticketUp() {

        return new TicketUp(UUID.randomUUID().toString(), UUID.randomUUID().toString(), Item.ESPRESSO, "Duck","baristaName");
    }
}
