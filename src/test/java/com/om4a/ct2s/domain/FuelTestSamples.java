package com.om4a.ct2s.domain;

import java.util.UUID;

public class FuelTestSamples {

    public static Fuel getFuelSample1() {
        return new Fuel().id("id1").name("name1");
    }

    public static Fuel getFuelSample2() {
        return new Fuel().id("id2").name("name2");
    }

    public static Fuel getFuelRandomSampleGenerator() {
        return new Fuel().id(UUID.randomUUID().toString()).name(UUID.randomUUID().toString());
    }
}
