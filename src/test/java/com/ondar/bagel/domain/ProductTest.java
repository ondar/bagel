package com.ondar.bagel.domain;

import static com.ondar.bagel.domain.OrderLineTestSamples.*;
import static com.ondar.bagel.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ondar.bagel.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void orderLineTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        OrderLine orderLineBack = getOrderLineRandomSampleGenerator();

        product.addOrderLine(orderLineBack);
        assertThat(product.getOrderLines()).containsOnly(orderLineBack);
        assertThat(orderLineBack.getProduct()).isEqualTo(product);

        product.removeOrderLine(orderLineBack);
        assertThat(product.getOrderLines()).doesNotContain(orderLineBack);
        assertThat(orderLineBack.getProduct()).isNull();

        product.orderLines(new HashSet<>(Set.of(orderLineBack)));
        assertThat(product.getOrderLines()).containsOnly(orderLineBack);
        assertThat(orderLineBack.getProduct()).isEqualTo(product);

        product.setOrderLines(new HashSet<>());
        assertThat(product.getOrderLines()).doesNotContain(orderLineBack);
        assertThat(orderLineBack.getProduct()).isNull();
    }
}
