package io.quarkuscoffeeshop.counter.infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkuscoffeeshop.counter.commands.CommandMocker;
import io.quarkuscoffeeshop.counter.domain.Order;
import io.quarkuscoffeeshop.counter.domain.OrderRepository;
import io.quarkuscoffeeshop.counter.domain.commands.PlaceOrderCommand;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderTicket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest @Transactional
public class OrderServiceTest {

    Logger logger = LoggerFactory.getLogger(OrderServiceTest.class);

    @InjectMock
    OrderRepository orderRepository;

    @Inject
    OrderService orderService;

    @Inject
    BaristaStream ticketStream;

    @Inject
    KitchenStream kitchenStream;

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
        assertEquals(ticketStream.getOrderTickets().size(), 1, "1 ticket should have been delivered to the 'barista' stream");
        logger.info("Ticket received {}", ticketStream.getOrderTickets().get(0));
        OrderTicket orderTicket = ticketStream.getOrderTickets().get(0);
        assertEquals(placeOrderCommand.getId(), orderTicket.getOrderId(), "The order id should be the same");
    }

    @Test
    public void testPlacingKitchenOnlyOrder() {

        PlaceOrderCommand placeOrderCommand = CommandMocker.placeOrderCommandSingleCroissant();

        logger.info("Testing order with: {}", placeOrderCommand);
        orderService.onPlaceOrderCommand(placeOrderCommand);
        assertEquals(kitchenStream.getOrderTickets().size(), 1, "1 ticket should have been delivered to the 'kitchen' stream");
        logger.info("Ticket received {}", kitchenStream.getOrderTickets().get(0));
        OrderTicket orderTicket = kitchenStream.getOrderTickets().get(0);
        assertEquals(placeOrderCommand.getId(), orderTicket.getOrderId(), "The order id should be the same");
    }

    @Test
    public void testPlacingBaristaAndKitchenOrder() {

        PlaceOrderCommand placeOrderCommand = CommandMocker.placeOrderCommandBlackCoffeeAndCroissant();

        logger.info("Testing order with: {}", placeOrderCommand);
        orderService.onPlaceOrderCommand(placeOrderCommand);
        assertEquals(ticketStream.getOrderTickets().size(), 1, "1 ticket should have been delivered to the 'barista' stream");
        logger.info("Ticket received {}", ticketStream.getOrderTickets().get(0));
        OrderTicket baristaTicket = ticketStream.getOrderTickets().get(0);
        assertEquals(placeOrderCommand.getId(), baristaTicket.getOrderId(), "The order id should be the same");
        assertEquals(kitchenStream.getOrderTickets().size(), 1, "1 ticket should have been delivered to the 'kitchen' stream");
        logger.info("Ticket received {}", kitchenStream.getOrderTickets().get(0));
        OrderTicket kitchenTicket = kitchenStream.getOrderTickets().get(0);
        assertEquals(placeOrderCommand.getId(), kitchenTicket.getOrderId(), "The order id should be the same");
    }
}
