package com.om4a.ct2s.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class YearTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Year getYearSample1() {
        return new Year().id("id1").year(1);
    }

    public static Year getYearSample2() {
        return new Year().id("id2").year(2);
    }

    public static Year getYearRandomSampleGenerator() {
        return new Year().id(UUID.randomUUID().toString()).year(intCount.incrementAndGet());
    }
}
