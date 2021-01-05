package io.quarkuscoffeeshop.counter.infrastructure;

import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderTicket;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Objects;

@ApplicationScoped
public class BaristaStream {

    private ArrayList<OrderTicket> orderTickets = new ArrayList<>();

    @Incoming("barista")
    public void baristaIn(final OrderTicket orderTicket) {
        this.orderTickets.add(orderTicket);
    }

    public BaristaStream() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaristaStream that = (BaristaStream) o;
        return Objects.equals(orderTickets, that.orderTickets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTickets);
    }

    public ArrayList<OrderTicket> getOrderTickets() {
        return orderTickets;
    }

    public void setOrderTickets(ArrayList<OrderTicket> orderTickets) {
        this.orderTickets = orderTickets;
    }
}
