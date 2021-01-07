package io.quarkuscoffeeshop.counter.infrastructure.messaging;

import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderTicket;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;

@ApplicationScoped
public class KitchenStreamListener extends StreamListener {

    @Incoming("kitchen")
    public void kitchenIn(final OrderTicket orderTicket) {
        this.objects.add(orderTicket);
    }

    @Override
    public ArrayList<OrderTicket> getObjects() {
        return this.objects;
    }

}
