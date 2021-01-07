package io.quarkuscoffeeshop.counter.infrastructure.messaging;

import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderTicket;

import java.util.ArrayList;
import java.util.Objects;

public abstract class StreamListener<T> {

    protected ArrayList<T> objects = new ArrayList();

    public StreamListener() {
    }

    public void reset() {
        this.objects = new ArrayList<>();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StreamListener that = (StreamListener) o;
        return Objects.equals(objects, that.objects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objects);
    }

    public ArrayList<T> getObjects() {
        return objects;
    }

    public void setOrderTickets(ArrayList<T> orderTickets) {
        this.objects = objects;
    }

}
