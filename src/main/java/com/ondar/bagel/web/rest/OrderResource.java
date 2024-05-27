package com.ondar.bagel.web.rest;

import com.ondar.bagel.domain.Order;
import com.ondar.bagel.repository.OrderRepository;
import com.ondar.bagel.service.OrderService;
import com.ondar.bagel.service.dto.OrderCreateResponse;
import com.ondar.bagel.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Operation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ondar.bagel.domain.Order}.
 */
@RestController
@RequestMapping("/api/orders")
@Transactional
public class OrderResource {

    private final Logger log = LoggerFactory.getLogger(OrderResource.class);

    private static final String ENTITY_NAME = "order";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public OrderResource(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    /**
     * {@code POST  /orders} : Create a new order.
     *
     * @param order the order to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new order, or with status {@code 400 (Bad Request)} if the order has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(description = "Create new order, decrease product stock")
    @PostMapping("")
    public ResponseEntity<?> createOrder(@RequestBody Order order) throws URISyntaxException {
        log.debug("REST request to save Order : {}", order);
        if (order.getId() != null) {
            throw new BadRequestAlertException("A new order cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderCreateResponse response = orderService.create(order);
        if (response.getOrder() != null) {
            return ResponseEntity.created(new URI("/api/orders/" + response.getOrder().getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, response.getOrder().getId().toString()))
                .body(response.getOrder());
        }
        return ResponseEntity.badRequest().body(response.getMessage());
    }

    /**
     * {@code GET  /orders} : get all the orders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orders in body.
     */
    @GetMapping("")
    public List<Order> getAllOrders() {
        log.debug("REST request to get all Orders");
        return orderRepository.findAll();
    }

    /**
     * {@code GET  /orders/:id} : get the "id" order.
     *
     * @param id the id of the order to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the order, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable("id") Long id) {
        log.debug("REST request to get Order : {}", id);
        Optional<Order> order = orderRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(order);
    }

    @Operation(description = "Pay for order, must be called before order expires")
    @PostMapping("/pay/{id}")
    public ResponseEntity<String> payOrder(@PathVariable("id") Long id) {
        log.debug("REST request to pay Order : {}", id);

        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String message = orderService.pay(orderOpt.get());
        if (message == null) {
            return ResponseEntity.noContent()
                .headers(HeaderUtil.createAlert(applicationName, "Order " + id + " was paid successfully", id.toString()))
                .build();
        }
        return ResponseEntity.badRequest().body(message);
    }

    @Operation(description = "Cancel order, return stock to product")
    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelOrder(@PathVariable("id") Long id) {
        log.debug("REST request to cancel Order : {}", id);

        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String message = orderService.cancel(orderOpt.get());
        if (message == null) {
            return ResponseEntity.noContent()
                .headers(HeaderUtil.createAlert(applicationName, "Order " + id + " was cancelled successfully", id.toString()))
                .build();
        }
        return ResponseEntity.badRequest().body(message);
    }
}
