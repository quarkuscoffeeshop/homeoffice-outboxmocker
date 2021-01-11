package io.quarkuscoffeeshop.counter.infrastructure;

import io.debezium.outbox.quarkus.ExportedEvent;
import io.quarkuscoffeeshop.counter.domain.Order;
import io.quarkuscoffeeshop.counter.domain.OrderRepository;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderEventResult;
import io.quarkuscoffeeshop.counter.domain.commands.PlaceOrderCommand;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderTicket;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderUpdate;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.context.ThreadContext;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class OrderService {

  Logger logger = LoggerFactory.getLogger(OrderService.class);

  @Inject
  ThreadContext threadContext;

  @Inject
  ManagedExecutor managedExecutor;

  @Inject
  ManagedExecutor managedExecutor2;

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
    public void anotherPlaceOrder(final PlaceOrderCommand placeOrderCommand) {

        logger.debug("onPlaceOrderCommand {}", placeOrderCommand);

        OrderEventResult orderEventResult = Order.process(placeOrderCommand);

        logger.debug("OrderEventResult returned: {}", orderEventResult);

        orderRepository.persist(orderEventResult.getOrder());
        orderEventResult.getOutboxEvents().forEach(exportedEvent -> {
            logger.debug("Firing event: {}", exportedEvent);
            event.fire(exportedEvent);
        });

    }

  @Transactional
  public void onPlaceOrderCommand(final PlaceOrderCommand placeOrderCommand) {

//    return CompletableFuture.runAsync(() -> {

      logger.debug("onPlaceOrderCommand {}", placeOrderCommand);

      OrderEventResult orderEventResult = Order.process(placeOrderCommand);

      logger.debug("OrderEventResult returned: {}", orderEventResult);

      orderRepository.persist(orderEventResult.getOrder());

      logger.debug("Order persisted");

/*
      orderEventResult.getOutboxEvents().forEach(exportedEvent -> {
        logger.debug("Firing event: {}", exportedEvent);
        event.fire(exportedEvent);
      });
*/

      if(orderEventResult.getBaristaTickets().isPresent()){
        orderEventResult.getBaristaTickets().get().forEach(baristaTicket -> {
          logger.debug("Sending Ticket to Barista Service: {}", baristaTicket);
          baristaEmitter.send(baristaTicket);
        });
      }

      if (orderEventResult.getKitchenTickets().isPresent()) {
        orderEventResult.getKitchenTickets().get().forEach(kitchenTicket -> {
          kitchenEmitter.send(kitchenTicket);
        });
      }
//    });
  }

  @Transactional
  public CompletableFuture<Void> processPlaceOrderCommand(final PlaceOrderCommand placeOrderCommand){

      logger.debug("processPlaceOrderCommand {}", placeOrderCommand);

      OrderEventResult orderEventResult = Order.process(placeOrderCommand);

      logger.debug("OrderEventResult returned: {}", orderEventResult);

/*
      orderRepository.persist(orderEventResult.getOrder());

      if(orderEventResult.getBaristaTickets().isPresent()){
          orderEventResult.getBaristaTickets().get().forEach(baristaTicket -> {
              logger.debug("Sending Ticket to Barista Service: {}", baristaTicket);
              baristaEmitter.send(baristaTicket);
          });
      }

      if (orderEventResult.getKitchenTickets().isPresent()) {
          orderEventResult.getKitchenTickets().get().forEach(kitchenTicket -> {
              kitchenEmitter.send(kitchenTicket);
          });
      }
*/
      return persistOrder(orderEventResult.getOrder())
              .thenRunAsync(
                      () -> {
                          orderEventResult.getOutboxEvents().forEach(exportedEvent -> {
                              event.fire(exportedEvent);
                              logger.debug("Fired event: {}", exportedEvent);
                          });
                      }
              )
              .thenRun(
                  () -> {
                  if(orderEventResult.getBaristaTickets().isPresent()){
                      orderEventResult.getBaristaTickets().get().forEach(baristaTicket -> {
                          baristaEmitter.send(baristaTicket);
                          logger.debug("Ticket sent to Barista Service: {}", baristaTicket);
                      });
                  }

                  if (orderEventResult.getKitchenTickets().isPresent()) {
                      orderEventResult.getKitchenTickets().get().forEach(kitchenTicket -> {
                          kitchenEmitter.send(kitchenTicket);
                          logger.debug("Ticket sent to Kitchen Service: {}", kitchenTicket);
                      });
                  }
              }
      );
  }

    CompletableFuture<Void> fireEvents(final List<ExportedEvent> events) {
        return CompletableFuture.supplyAsync(() -> {
            events.forEach(exportedEvent -> {
                event.fire(exportedEvent);
                logger.debug("Fired event: {}", exportedEvent);
            });
            return null;
        }, managedExecutor);
    }

  CompletableFuture<Void> persistOrder(final Order order) {
    return CompletableFuture.supplyAsync(() -> {
        orderRepository.persist(order);
        logger.debug("order persisted: {}", order);
        return null;
    }, managedExecutor2);
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
