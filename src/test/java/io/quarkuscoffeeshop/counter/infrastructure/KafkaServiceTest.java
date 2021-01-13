package io.quarkuscoffeeshop.counter.infrastructure;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.junit.mockito.InjectSpy;
import io.quarkuscoffeeshop.counter.commands.CommandMocker;
import io.quarkuscoffeeshop.counter.domain.OrderMocker;
import io.quarkuscoffeeshop.counter.domain.OrderRepository;
import io.quarkuscoffeeshop.counter.domain.commands.PlaceOrderCommand;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static org.mockito.ArgumentMatchers.any;

@QuarkusTest @Transactional
public class KafkaServiceTest {

    @InjectSpy
    OrderService orderService;

    @InjectMock
    OrderRepository orderRepository;

    @Inject
    @Channel("orders-in")
    Emitter<PlaceOrderCommand> ordersInEmitter;

    @BeforeEach
    public void setUp() {

        Mockito.when(orderRepository.findById(any(String.class))).thenReturn(OrderMocker.mockOrder());
    }

    @Test
    public void testOrderIn() {

//        ordersInEmitter.send(CommandMocker.placeOrderCommandSingleBlackCoffee());
        Mockito.verify(orderService, Mockito.times(1)).onOrderIn(any(PlaceOrderCommand.class));
    }

}
