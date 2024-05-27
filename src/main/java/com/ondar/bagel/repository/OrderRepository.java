package com.ondar.bagel.repository;

import com.ondar.bagel.domain.Order;
import java.util.Collection;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Order entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Collection<Order> findAllByPaidIsNullAndCancelledIsNull();
}
