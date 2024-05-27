package com.ondar.bagel.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * An OrderLine.
 */
@Entity
@Table(name = "order_line")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderLine implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "quantity")
    private Integer quantity;

    @NotNull
    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orderLines" }, allowSetters = true)
    private Product product;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orderLines" }, allowSetters = true)
    private Order order;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderLine id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public OrderLine quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
        price = product.getPrice();
    }

    public OrderLine product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderLine order(Order order) {
        this.setOrder(order);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderLine)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderLine) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderLine{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", price=" + getPrice() +
            ", product=" + getProduct().getName() +
            ", order=" + (getOrder() == null ? null : getOrder().getId()) +
            "}";
    }
}
