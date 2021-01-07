package io.quarkuscoffeeshop.counter.infrastructure.messaging;

import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderUpdate;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Objects;

@ApplicationScoped
public class OrderUpdatesStream {

    protected ArrayList<OrderUpdate> orderUpdates = new ArrayList<>();

    @Incoming("order-updates")
    public void onOrderUpdate(final OrderUpdate orderUpdate) {
        this.orderUpdates.add(orderUpdate);
    }

    public OrderUpdatesStream() {
    }

    public void reset() {
        this.orderUpdates = new ArrayList<>();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderUpdatesStream that = (OrderUpdatesStream) o;
        return Objects.equals(orderUpdates, that.orderUpdates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderUpdates);
    }

    public ArrayList<OrderUpdate> getOrderUpdates() {
        return orderUpdates;
    }

    public void setOrderUpdates(ArrayList<OrderUpdate> orderUpdates) {
        this.orderUpdates = orderUpdates;
    }

}
