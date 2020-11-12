package io.quarkuscoffeeshop.counter.infrastructure;

import io.debezium.outbox.quarkus.ExportedEvent;
import io.quarkuscoffeeshop.counter.domain.Order;
import io.quarkuscoffeeshop.counter.domain.OrderEvent;
import io.quarkuscoffeeshop.counter.domain.PlaceOrderCommand;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class OrderService {

    @Inject
    Event<ExportedEvent<?, ?>> event;

    @Transactional
    public void onPlaceOrderCommand(final PlaceOrderCommand placeOrderCommand) {

        OrderEvent orderEvent = Order.process(placeOrderCommand);
        orderEvent.getOrder().persist();
        orderEvent.getEvents().forEach(exportedEvent -> {
            event.fire(exportedEvent);
        });
    }
}
