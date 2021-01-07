package io.quarkuscoffeeshop.counter.infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkuscoffeeshop.counter.domain.*;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderTicket;
import io.quarkuscoffeeshop.counter.infrastructure.messaging.OrderUpdatesStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@QuarkusTest @Transactional
public class OrderServiceOrderUpTest {

    Logger logger = LoggerFactory.getLogger(OrderServiceTest.class);

    @Inject
    OrderService orderService;

    @InjectMock
    OrderRepository orderRepository;

    @Inject
    OrderUpdatesStream orderUpdatesStream;

    Order order = OrderMocker.mockOrder();

    @BeforeEach
    public void setUp() {
        logger.info("Mocked Order: {}", order);
        Mockito.when(orderRepository.findById(any(String.class))).thenReturn(order);
    }

    @Test
    public void testOnOrderUpdate() {

        LineItem lineItem = order.getBaristaLineItems().get().get(0);
        OrderTicket orderTicket = new OrderTicket(order.getOrderId(), lineItem.getItemId(), lineItem.getItem(), lineItem.getName());
        logger.info("Testing OrderTicket: {}", orderTicket);

        orderService.onOrderUp(orderTicket);
        verify(orderRepository).persist(order);
        assertEquals(orderUpdatesStream.getOrderUpdates().size(), 1, "1 update should have been delivered to the 'order-updates' stream");
    }
}
