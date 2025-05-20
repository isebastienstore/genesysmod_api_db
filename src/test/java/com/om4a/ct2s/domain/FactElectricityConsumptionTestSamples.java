package com.om4a.ct2s.domain;

import java.util.UUID;

public class FactElectricityConsumptionTestSamples {

    public static FactElectricityConsumption getFactElectricityConsumptionSample1() {
        return new FactElectricityConsumption().id("id1");
    }

    public static FactElectricityConsumption getFactElectricityConsumptionSample2() {
        return new FactElectricityConsumption().id("id2");
    }

    public static FactElectricityConsumption getFactElectricityConsumptionRandomSampleGenerator() {
        return new FactElectricityConsumption().id(UUID.randomUUID().toString());
    }
}
