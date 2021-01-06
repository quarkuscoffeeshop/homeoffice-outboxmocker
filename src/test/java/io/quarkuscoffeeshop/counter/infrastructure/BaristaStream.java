package io.quarkuscoffeeshop.counter.infrastructure;

import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderTicket;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BaristaStream extends TicketStream{

    @Incoming("barista")
    public void kitchenIn(final OrderTicket orderTicket) {
        this.orderTickets.add(orderTicket);
    }
}