package io.quarkuscoffeeshop.counter.infrastructure;

import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderTicket;

import java.util.ArrayList;
import java.util.Objects;

public abstract class TicketStream {

    protected ArrayList<OrderTicket> orderTickets = new ArrayList<>();

    public TicketStream() {
    }

    public void reset() {
        this.orderTickets = new ArrayList<>();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketStream that = (TicketStream) o;
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
