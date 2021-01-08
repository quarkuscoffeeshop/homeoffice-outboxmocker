package io.quarkuscoffeeshop.counter.infrastructure;

import io.quarkuscoffeeshop.counter.domain.commands.PlaceOrderCommand;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderTicket;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class KafkaService {

    Logger logger = LoggerFactory.getLogger(KafkaService.class);

    @Inject
    OrderService orderService;

    @Incoming("orders-in")
    @Transactional
    public void orderIn(final PlaceOrderCommand placeOrderCommand) {
        System.out.println(placeOrderCommand);
        logger.debug("PlaceOrderCommand received: {}", placeOrderCommand);
        orderService.onPlaceOrderCommand(placeOrderCommand);
    }

    @Incoming("orders-up")
    @Transactional
    public void orderUp(final OrderTicket orderTicket) {
        logger.debug("OrderTicket received: {}", orderTicket);
        orderService.onOrderUp(orderTicket);
    }
}
