package io.quarkuscoffeeshop.counter.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class AssignedPK implements Serializable {

  private final String id;

  public AssignedPK() {
    this.id = UUID.randomUUID().toString();
  }

  public String getId() {
    return id;
  }
}
