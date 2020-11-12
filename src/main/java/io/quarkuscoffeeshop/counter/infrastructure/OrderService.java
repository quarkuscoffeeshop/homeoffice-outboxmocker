package io.quarkuscoffeeshop.counter.infrastructure;

import io.debezium.outbox.quarkus.ExportedEvent;
import io.quarkuscoffeeshop.counter.domain.Order;
import io.quarkuscoffeeshop.counter.domain.OrderEvent;
import io.quarkuscoffeeshop.counter.domain.PlaceOrderCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class OrderService {

    Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Inject
    Event<ExportedEvent<?, ?>> event;

    @Transactional
    public void onPlaceOrderCommand(final PlaceOrderCommand placeOrderCommand) {

        logger.debug("onPlaceOrderCommand {}", placeOrderCommand);
        OrderEvent orderEvent = Order.process(placeOrderCommand);
        orderEvent.getOrder().persist();
        orderEvent.getEvents().forEach(exportedEvent -> {
            event.fire(exportedEvent);
        });
    }
}
