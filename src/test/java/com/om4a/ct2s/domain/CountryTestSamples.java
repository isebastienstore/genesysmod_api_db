package com.om4a.ct2s.domain;

import java.util.UUID;

public class CountryTestSamples {

    public static Country getCountrySample1() {
        return new Country().id("id1").code("code1").name("name1");
    }

    public static Country getCountrySample2() {
        return new Country().id("id2").code("code2").name("name2");
    }

    public static Country getCountryRandomSampleGenerator() {
        return new Country().id(UUID.randomUUID().toString()).code(UUID.randomUUID().toString()).name(UUID.randomUUID().toString());
    }
}
