package com.ondar.bagel.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OrderLineTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static OrderLine getOrderLineSample1() {
        return new OrderLine().id(1L).quantity(1);
    }

    public static OrderLine getOrderLineSample2() {
        return new OrderLine().id(2L).quantity(2);
    }

    public static OrderLine getOrderLineRandomSampleGenerator() {
        return new OrderLine().id(longCount.incrementAndGet()).quantity(intCount.incrementAndGet());
    }
}
