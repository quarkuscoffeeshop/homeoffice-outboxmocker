package io.quarkuscoffeeshop.counter.infrastructure;

import io.debezium.outbox.quarkus.ExportedEvent;
import io.quarkuscoffeeshop.counter.domain.Order;
import io.quarkuscoffeeshop.counter.domain.OrderRepository;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderEventResult;
import io.quarkuscoffeeshop.counter.domain.commands.PlaceOrderCommand;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderTicket;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderUpdate;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
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
  OrderRepository orderRepository;

  @Inject
  Event<ExportedEvent<?, ?>> event;

  @Channel("barista")
  Emitter<OrderTicket> baristaEmitter;

  @Channel("kitchen")
  Emitter<OrderTicket> kitchenEmitter;

  @Channel("order-updates")
  Emitter<OrderUpdate> orderUpdateEmitter;

  @Transactional
  public void onPlaceOrderCommand(final PlaceOrderCommand placeOrderCommand) {

    logger.debug("onPlaceOrderCommand {}", placeOrderCommand);
    OrderEventResult orderEventResult = Order.process(placeOrderCommand);
    logger.debug("OrderEventResult returned: {}", orderEventResult);
    orderRepository.persist(orderEventResult.getOrder());
    orderEventResult.getOutboxEvents().forEach(exportedEvent -> {
      event.fire(exportedEvent);
    });
    if(orderEventResult.getBaristaTickets().isPresent()){
      orderEventResult.getBaristaTickets().get().forEach(baristaTicket -> {
        baristaEmitter.send(baristaTicket);
      });
    }
    if (orderEventResult.getKitchenTickets().isPresent()) {
      orderEventResult.getKitchenTickets().get().forEach(kitchenTicket -> {
        kitchenEmitter.send(kitchenTicket);
      });
    }
  }

  @Transactional
  public void onOrderUp(final OrderTicket orderTicket) {

    logger.debug("onOrderUp: {}", orderTicket);
    Order order = orderRepository.findById(orderTicket.getOrderId());
    OrderEventResult orderEventResult = order.applyOrderTicketUp(orderTicket);
    logger.debug("OrderEventResult returned: {}", orderEventResult);
    orderRepository.persist(orderEventResult.getOrder());
    orderEventResult.getOutboxEvents().forEach(exportedEvent -> {
      event.fire(exportedEvent);
    });
    orderEventResult.getOrderUpdates().forEach(orderUpdate -> {
      orderUpdateEmitter.send(orderUpdate);
    });
  }
}
