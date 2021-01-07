package io.quarkuscoffeeshop.counter.infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkuscoffeeshop.counter.commands.CommandMocker;
import io.quarkuscoffeeshop.counter.domain.OrderRepository;
import io.quarkuscoffeeshop.counter.domain.commands.PlaceOrderCommand;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderTicket;
import io.quarkuscoffeeshop.counter.infrastructure.messaging.BaristaStreamListener;
import io.quarkuscoffeeshop.counter.infrastructure.messaging.KitchenStreamListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest @Transactional
public class OrderServiceTest {

    Logger logger = LoggerFactory.getLogger(OrderServiceTest.class);

    @InjectMock
    OrderRepository orderRepository;

    @Inject
    OrderService orderService;

    @Inject
    BaristaStreamListener ticketStream;

    @Inject
    KitchenStreamListener kitchenStream;

    @AfterEach
    public void reset() {
        ticketStream.reset();
        kitchenStream.reset();
    }

    @Test
    public void testPlacingBaristaOnlyOrder() {

        PlaceOrderCommand placeOrderCommand = CommandMocker.placeOrderCommandSingleBlackCoffee();

        logger.info("Testing order with: {}", placeOrderCommand);
        orderService.onPlaceOrderCommand(placeOrderCommand);
        assertEquals(ticketStream.getObjects().size(), 1, "1 ticket should have been delivered to the 'barista' stream");
        logger.info("Ticket received {}", ticketStream.getObjects().get(0));
        OrderTicket orderTicket = ticketStream.getObjects().get(0);
        assertEquals(placeOrderCommand.getId(), orderTicket.getOrderId(), "The order id should be the same");
    }

    @Test
    public void testPlacingKitchenOnlyOrder() {

        PlaceOrderCommand placeOrderCommand = CommandMocker.placeOrderCommandSingleCroissant();

        logger.info("Testing order with: {}", placeOrderCommand);
        orderService.onPlaceOrderCommand(placeOrderCommand);
        assertEquals(kitchenStream.getObjects().size(), 1, "1 ticket should have been delivered to the 'kitchen' stream");
        logger.info("Ticket received {}", kitchenStream.getObjects().get(0));
        OrderTicket orderTicket = kitchenStream.getObjects().get(0);
        assertEquals(placeOrderCommand.getId(), orderTicket.getOrderId(), "The order id should be the same");
    }

    @Test
    public void testPlacingBaristaAndKitchenOrder() {

        PlaceOrderCommand placeOrderCommand = CommandMocker.placeOrderCommandBlackCoffeeAndCroissant();

        logger.info("Testing order with: {}", placeOrderCommand);
        orderService.onPlaceOrderCommand(placeOrderCommand);
        assertEquals(ticketStream.getObjects().size(), 1, "1 ticket should have been delivered to the 'barista' stream");
        logger.info("Ticket received {}", ticketStream.getObjects().get(0));
        OrderTicket baristaTicket = ticketStream.getObjects().get(0);
        assertEquals(placeOrderCommand.getId(), baristaTicket.getOrderId(), "The order id should be the same");
        assertEquals(kitchenStream.getObjects().size(), 1, "1 ticket should have been delivered to the 'kitchen' stream");
        logger.info("Ticket received {}", kitchenStream.getObjects().get(0));
        OrderTicket kitchenTicket = kitchenStream.getObjects().get(0);
        assertEquals(placeOrderCommand.getId(), kitchenTicket.getOrderId(), "The order id should be the same");
    }
}
