package com.om4a.ct2s.domain;

import java.util.UUID;

public class FactPowerProductionTestSamples {

    public static FactPowerProduction getFactPowerProductionSample1() {
        return new FactPowerProduction().id("id1");
    }

    public static FactPowerProduction getFactPowerProductionSample2() {
        return new FactPowerProduction().id("id2");
    }

    public static FactPowerProduction getFactPowerProductionRandomSampleGenerator() {
        return new FactPowerProduction().id(UUID.randomUUID().toString());
    }
}
