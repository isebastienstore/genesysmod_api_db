package com.om4a.ct2s.domain;

import java.util.UUID;

public class FactTradeCostTestSamples {

    public static FactTradeCost getFactTradeCostSample1() {
        return new FactTradeCost().id("id1");
    }

    public static FactTradeCost getFactTradeCostSample2() {
        return new FactTradeCost().id("id2");
    }

    public static FactTradeCost getFactTradeCostRandomSampleGenerator() {
        return new FactTradeCost().id(UUID.randomUUID().toString());
    }
}
