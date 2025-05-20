package com.om4a.ct2s.domain;

import java.util.UUID;

public class FactTransportTestSamples {

    public static FactTransport getFactTransportSample1() {
        return new FactTransport().id("id1");
    }

    public static FactTransport getFactTransportSample2() {
        return new FactTransport().id("id2");
    }

    public static FactTransport getFactTransportRandomSampleGenerator() {
        return new FactTransport().id(UUID.randomUUID().toString());
    }
}
