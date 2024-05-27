package com.ondar.bagel.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * An Order.
 */
@Entity
@Table(name = "jhi_order")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final int EXPIRY_SEC = 30 * 60; // how long till unpaid order expires and is cancelled

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "created")
    private Instant created = Instant.now();

    @Column(name = "paid")
    private Instant paid;

    @Column(name = "cancelled")
    private Instant cancelled;

    @NotNull
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order")
    @JsonIgnoreProperties(value = { "order" }, allowSetters = true)
    private Set<OrderLine> orderLines = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderLine line : orderLines) {
            total = total.add(line.getPrice().multiply(BigDecimal.valueOf(line.getQuantity())));
        }
        return total;
    }

    public String getExpiresIn() {
        if (cancelled != null) {
            return "cancelled";
        }
        if (paid != null) {
            return "paid";
        }
        long expiresIn = getExpiresInSec();
        if (expiresIn <= 0) {
            return "now";
        }
        if (expiresIn > 60) {
            return (expiresIn / 60) + "m " + (expiresIn % 60) + "s";
        }
        return expiresIn + "s";
    }

    private long getExpiresInSec() {
        return EXPIRY_SEC - Instant.now().getEpochSecond() + created.getEpochSecond();
    }

    public boolean isExpired() {
        return paid == null && cancelled == null && getExpiresInSec() <= 0;
    }

    public Long getId() {
        return this.id;
    }

    public Order id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreated() {
        return this.created;
    }

    public Instant getPaid() {
        return this.paid;
    }

    public Order paid(Instant paid) {
        this.setPaid(paid);
        return this;
    }

    public void setPaid(Instant paid) {
        this.paid = paid;
    }

    public Instant getCancelled() {
        return cancelled;
    }

    public void setCancelled(Instant cancelled) {
        this.cancelled = cancelled;
    }

    public Set<OrderLine> getOrderLines() {
        return this.orderLines;
    }

    public void setOrderLines(Set<OrderLine> orderLines) {
        if (this.orderLines != null) {
            this.orderLines.forEach(i -> i.setOrder(null));
        }
        if (orderLines != null) {
            orderLines.forEach(i -> i.setOrder(this));
        }
        this.orderLines = orderLines;
    }

    public Order orderLines(Set<OrderLine> orderLines) {
        this.setOrderLines(orderLines);
        return this;
    }

    public Order addOrderLine(OrderLine orderLine) {
        this.orderLines.add(orderLine);
        orderLine.setOrder(this);
        return this;
    }

    public Order removeOrderLine(OrderLine orderLine) {
        this.orderLines.remove(orderLine);
        orderLine.setOrder(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return getId() != null && getId().equals(((Order) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", paid='" + getPaid() + "'" +
            ", cancelled='" + getCancelled() + "'" +
            ", expiresIn='" + getExpiresIn() + "'" +
            ", lines=" + getOrderLines() +
            "}";
    }
}
