package com.ondar.bagel.job;

import com.ondar.bagel.domain.Order;
import com.ondar.bagel.repository.OrderRepository;
import com.ondar.bagel.service.OrderService;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Task that finds all expired orders and cancels them.
 */
@Component
public class OrderCancelTask {

    private final Logger log = LoggerFactory.getLogger(OrderCancelTask.class);

    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public OrderCancelTask(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.SECONDS)
    @Transactional
    public void cancelTask() {
        Collection<Order> orders = orderRepository.findAllByPaidIsNullAndCancelledIsNull();
        log.debug("Got {} orders to check cancellation: {}", orders.size(), orders);
        for (Order order : orders) {
            if (order.isExpired()) {
                orderService.cancel(order);
            }
        }
    }
}
