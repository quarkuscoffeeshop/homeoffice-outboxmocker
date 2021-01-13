package io.quarkuscoffeeshop.counter.infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.junit.mockito.InjectSpy;
import io.quarkuscoffeeshop.counter.domain.Order;
import io.quarkuscoffeeshop.counter.domain.OrderMocker;
import io.quarkuscoffeeshop.counter.domain.OrderRepository;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderTicket;
import io.quarkuscoffeeshop.counter.domain.valueobjects.TicketUp;
import io.smallrye.reactive.messaging.annotations.Merge;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.transaction.Transactional;

import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
@Transactional
public class KafkaServiceOrderUpTest {

    @InjectSpy
    OrderService orderService;

    @InjectMock
    OrderRepository orderRepository;

    @Channel("orders-up")
    Emitter<TicketUp> ordersUpEmitter;

    @BeforeEach
    public void setUp() {

        Mockito.when(orderRepository.findById(any(String.class))).thenReturn(OrderMocker.mockOrder());
    }

    @Test
    public void testOrderUp() {

        TicketUp ticketUp = new TicketUp(OrderMocker.mockOrder().getOrderId(), OrderMocker.mockOrder().getBaristaLineItems().get().get(0).getItemId(), OrderMocker.mockOrder().getBaristaLineItems().get().get(0).getItem(), OrderMocker.mockOrder().getBaristaLineItems().get().get(0).getName(), "baristaName");
        ordersUpEmitter.send(ticketUp);
        Mockito.verify(orderService, Mockito.times(1)).onOrderUp(any(TicketUp.class));
    }
}
