package com.ondar.bagel.service.dto;

import com.ondar.bagel.domain.Order;

public class OrderCreateResponse {

    private final Order order;
    private final String message;

    public OrderCreateResponse(Order order) {
        this.order = order;
        message = null;
    }

    public OrderCreateResponse(String message) {
        this.order = null;
        this.message = message;
    }

    public Order getOrder() {
        return order;
    }

    public String getMessage() {
        return message;
    }
}
