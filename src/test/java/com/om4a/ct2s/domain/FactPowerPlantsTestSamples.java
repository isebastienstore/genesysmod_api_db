package com.om4a.ct2s.domain;

import java.util.UUID;

public class FactPowerPlantsTestSamples {

    public static FactPowerPlants getFactPowerPlantsSample1() {
        return new FactPowerPlants().id("id1").name("name1");
    }

    public static FactPowerPlants getFactPowerPlantsSample2() {
        return new FactPowerPlants().id("id2").name("name2");
    }

    public static FactPowerPlants getFactPowerPlantsRandomSampleGenerator() {
        return new FactPowerPlants().id(UUID.randomUUID().toString()).name(UUID.randomUUID().toString());
    }
}
