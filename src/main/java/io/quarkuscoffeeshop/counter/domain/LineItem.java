package io.quarkuscoffeeshop.counter.domain;

import java.util.Objects;
import java.util.StringJoiner;

public class LineItem {

    private Item item;

    private String name;

    public LineItem() {
    }

    public LineItem(Item item, String name) {
        this.item = item;
        this.name = name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LineItem.class.getSimpleName() + "[", "]")
                .add("item=" + item)
                .add("name='" + name + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineItem lineItem = (LineItem) o;
        return item == lineItem.item &&
                Objects.equals(name, lineItem.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, name);
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
