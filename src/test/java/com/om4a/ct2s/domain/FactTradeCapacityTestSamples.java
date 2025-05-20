package com.om4a.ct2s.domain;

import java.util.UUID;

public class FactTradeCapacityTestSamples {

    public static FactTradeCapacity getFactTradeCapacitySample1() {
        return new FactTradeCapacity().id("id1");
    }

    public static FactTradeCapacity getFactTradeCapacitySample2() {
        return new FactTradeCapacity().id("id2");
    }

    public static FactTradeCapacity getFactTradeCapacityRandomSampleGenerator() {
        return new FactTradeCapacity().id(UUID.randomUUID().toString());
    }
}
