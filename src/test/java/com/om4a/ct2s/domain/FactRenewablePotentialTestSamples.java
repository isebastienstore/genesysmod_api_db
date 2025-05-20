package com.om4a.ct2s.domain;

import java.util.UUID;

public class FactRenewablePotentialTestSamples {

    public static FactRenewablePotential getFactRenewablePotentialSample1() {
        return new FactRenewablePotential().id("id1");
    }

    public static FactRenewablePotential getFactRenewablePotentialSample2() {
        return new FactRenewablePotential().id("id2");
    }

    public static FactRenewablePotential getFactRenewablePotentialRandomSampleGenerator() {
        return new FactRenewablePotential().id(UUID.randomUUID().toString());
    }
}
