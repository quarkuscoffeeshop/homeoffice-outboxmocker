package io.quarkuscoffeeshop.counter.infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.junit.mockito.InjectSpy;
import io.quarkuscoffeeshop.counter.commands.CommandMocker;
import io.quarkuscoffeeshop.counter.domain.Order;
import io.quarkuscoffeeshop.counter.domain.OrderMocker;
import io.quarkuscoffeeshop.counter.domain.OrderRepository;
import io.quarkuscoffeeshop.counter.domain.commands.PlaceOrderCommand;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderTicket;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.transaction.Transactional;

import static org.mockito.ArgumentMatchers.any;

@QuarkusTest @Transactional
public class KafkaServiceTest {

    @InjectSpy
    OrderService orderService;

    @InjectMock
    OrderRepository orderRepository;

    @Channel("orders-in")
    Emitter ordersInEmitter;

    @Channel("orders-up")
    Emitter ordersUpEmitter;

    Order order;

    OrderTicket orderTicket;

    @BeforeEach
    public void setUp() {

        order = OrderMocker.mockOrder();
        orderTicket = new OrderTicket(order.getOrderId(),
                order.getBaristaLineItems().get().get(0).getItemId(),
                order.getBaristaLineItems().get().get(0).getItem(),
                order.getBaristaLineItems().get().get(0).getName());

        Mockito.when(orderRepository.findById(any(String.class))).thenReturn(order);
    }

    @Test
    public void testOrderIn() {

        PlaceOrderCommand placeOrderCommand = CommandMocker.placeOrderCommandSingleBlackCoffee();
        ordersInEmitter.send(placeOrderCommand);
        Mockito.verify(orderService, Mockito.times(1)).onPlaceOrderCommand(any(PlaceOrderCommand.class));
    }

    @Test
    public void testOrderUp() {

        ordersUpEmitter.send(orderTicket);
        Mockito.verify(orderService, Mockito.times(1)).onOrderUp(any(OrderTicket.class));
    }
}
