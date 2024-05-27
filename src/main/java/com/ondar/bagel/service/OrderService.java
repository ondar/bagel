package com.ondar.bagel.service;

import com.ondar.bagel.domain.Order;
import com.ondar.bagel.domain.OrderLine;
import com.ondar.bagel.domain.Product;
import com.ondar.bagel.repository.OrderLineRepository;
import com.ondar.bagel.repository.OrderRepository;
import com.ondar.bagel.repository.ProductRepository;
import com.ondar.bagel.service.dto.OrderCreateResponse;
import java.time.Instant;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing orders.
 */
@Service
@Transactional
public class OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, OrderLineRepository orderLineRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderLineRepository = orderLineRepository;
        this.productRepository = productRepository;
    }

    /**
     * Create new order, decrease product stock
     *
     * @param order contains order lines to create order from
     * @return object with error message or null if create success, created order or null if error
     */
    public OrderCreateResponse create(Order order) {
        if (order.getOrderLines().isEmpty()) {
            return new OrderCreateResponse("A new order must have order lines");
        }

        StringBuilder sb = new StringBuilder();
        for (OrderLine line : order.getOrderLines()) {
            Product product = line.getProduct();
            // enough product quantity on stock?
            if (product.getStock() < line.getQuantity()) {
                if (!sb.isEmpty()) {
                    sb.append(", ");
                }
                sb.append(product.getName());
            }
        }
        if (!sb.isEmpty()) {
            log.info("Items not in stock: {}", sb);
            return new OrderCreateResponse("Products without sufficient stock: " + sb);
        }

        order = orderRepository.save(order);
        for (OrderLine line : order.getOrderLines()) {
            line.setOrder(order);
            orderLineRepository.save(line);

            Product product = line.getProduct();
            // decrease product stock
            product.setStock(product.getStock() - line.getQuantity());
            productRepository.save(product);
        }
        return new OrderCreateResponse(order);
    }

    /**
     * Pay the order
     *
     * @param order order to pay
     * @return error message or null if create success
     */
    public String pay(Order order) {
        if (order.getPaid() != null) {
            return "Order " + order.getId() + " was already paid";
        }
        if (order.getCancelled() != null) {
            return "Order " + order.getId() + " was cancelled and cannot be paid";
        }
        if (order.isExpired()) {
            return "Order " + order.getId() + " has expired and cannot be paid";
        }

        order.setPaid(Instant.now());
        orderRepository.save(order);

        log.info("Order {} paid", order.getId());
        return null;
    }

    /**
     * Cancel order and return stock to product
     *
     * @param order order to cancel
     * @return error message or null if create success
     */
    public String cancel(Order order) {
        if (order.getCancelled() != null) {
            return "Order " + order.getId() + " was already cancelled";
        }
        if (order.getPaid() != null) {
            return "Order " + order.getId() + " was paid and cannot be cancelled";
        }

        // return products to stock
        for (OrderLine line : order.getOrderLines()) {
            Product product = line.getProduct();
            product.setStock(product.getStock() + line.getQuantity());
            productRepository.save(product);
        }

        order.setCancelled(Instant.now());
        orderRepository.save(order);

        log.info("Order {} cancelled", order.getId());
        return null;
    }
}
