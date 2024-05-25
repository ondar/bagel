package com.ondar.bagel.domain;

import static com.ondar.bagel.domain.OrderLineTestSamples.*;
import static com.ondar.bagel.domain.OrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ondar.bagel.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Order.class);
        Order order1 = getOrderSample1();
        Order order2 = new Order();
        assertThat(order1).isNotEqualTo(order2);

        order2.setId(order1.getId());
        assertThat(order1).isEqualTo(order2);

        order2 = getOrderSample2();
        assertThat(order1).isNotEqualTo(order2);
    }

    @Test
    void orderLineTest() throws Exception {
        Order order = getOrderRandomSampleGenerator();
        OrderLine orderLineBack = getOrderLineRandomSampleGenerator();

        order.addOrderLine(orderLineBack);
        assertThat(order.getOrderLines()).containsOnly(orderLineBack);
        assertThat(orderLineBack.getOrder()).isEqualTo(order);

        order.removeOrderLine(orderLineBack);
        assertThat(order.getOrderLines()).doesNotContain(orderLineBack);
        assertThat(orderLineBack.getOrder()).isNull();

        order.orderLines(new HashSet<>(Set.of(orderLineBack)));
        assertThat(order.getOrderLines()).containsOnly(orderLineBack);
        assertThat(orderLineBack.getOrder()).isEqualTo(order);

        order.setOrderLines(new HashSet<>());
        assertThat(order.getOrderLines()).doesNotContain(orderLineBack);
        assertThat(orderLineBack.getOrder()).isNull();
    }
}
