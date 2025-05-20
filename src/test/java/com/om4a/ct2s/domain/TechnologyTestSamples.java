package com.om4a.ct2s.domain;

import java.util.UUID;

public class TechnologyTestSamples {

    public static Technology getTechnologySample1() {
        return new Technology().id("id1").name("name1");
    }

    public static Technology getTechnologySample2() {
        return new Technology().id("id2").name("name2");
    }

    public static Technology getTechnologyRandomSampleGenerator() {
        return new Technology().id(UUID.randomUUID().toString()).name(UUID.randomUUID().toString());
    }
}
