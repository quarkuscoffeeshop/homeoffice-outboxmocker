package io.quarkuscoffeeshop.counter.infrastructure;

import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderTicket;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Objects;

@ApplicationScoped
public class KitchenStream extends TicketStream{

    @Incoming("kitchen")
    public void kitchenIn(final OrderTicket orderTicket) {
        this.orderTickets.add(orderTicket);
    }

}
