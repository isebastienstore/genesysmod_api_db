package com.om4a.ct2s.domain;

import java.util.UUID;

public class MetadataTestSamples {

    public static Metadata getMetadataSample1() {
        return new Metadata().id("id1").createdBy("createdBy1").updatedBy("updatedBy1").source("source1");
    }

    public static Metadata getMetadataSample2() {
        return new Metadata().id("id2").createdBy("createdBy2").updatedBy("updatedBy2").source("source2");
    }

    public static Metadata getMetadataRandomSampleGenerator() {
        return new Metadata()
            .id(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .updatedBy(UUID.randomUUID().toString())
            .source(UUID.randomUUID().toString());
    }
}
