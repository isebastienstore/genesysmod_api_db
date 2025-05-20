package com.om4a.ct2s.domain;

import static com.om4a.ct2s.domain.CountryTestSamples.*;
import static com.om4a.ct2s.domain.FactTradeCapacityTestSamples.*;
import static com.om4a.ct2s.domain.FuelTestSamples.*;
import static com.om4a.ct2s.domain.MetadataTestSamples.*;
import static com.om4a.ct2s.domain.YearTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.om4a.ct2s.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FactTradeCapacityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactTradeCapacity.class);
        FactTradeCapacity factTradeCapacity1 = getFactTradeCapacitySample1();
        FactTradeCapacity factTradeCapacity2 = new FactTradeCapacity();
        assertThat(factTradeCapacity1).isNotEqualTo(factTradeCapacity2);

        factTradeCapacity2.setId(factTradeCapacity1.getId());
        assertThat(factTradeCapacity1).isEqualTo(factTradeCapacity2);

        factTradeCapacity2 = getFactTradeCapacitySample2();
        assertThat(factTradeCapacity1).isNotEqualTo(factTradeCapacity2);
    }

    @Test
    void yearTest() {
        FactTradeCapacity factTradeCapacity = getFactTradeCapacityRandomSampleGenerator();
        Year yearBack = getYearRandomSampleGenerator();

        factTradeCapacity.setYear(yearBack);
        assertThat(factTradeCapacity.getYear()).isEqualTo(yearBack);

        factTradeCapacity.year(null);
        assertThat(factTradeCapacity.getYear()).isNull();
    }

    @Test
    void country1Test() {
        FactTradeCapacity factTradeCapacity = getFactTradeCapacityRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        factTradeCapacity.setCountry1(countryBack);
        assertThat(factTradeCapacity.getCountry1()).isEqualTo(countryBack);

        factTradeCapacity.country1(null);
        assertThat(factTradeCapacity.getCountry1()).isNull();
    }

    @Test
    void country2Test() {
        FactTradeCapacity factTradeCapacity = getFactTradeCapacityRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        factTradeCapacity.setCountry2(countryBack);
        assertThat(factTradeCapacity.getCountry2()).isEqualTo(countryBack);

        factTradeCapacity.country2(null);
        assertThat(factTradeCapacity.getCountry2()).isNull();
    }

    @Test
    void fuelTest() {
        FactTradeCapacity factTradeCapacity = getFactTradeCapacityRandomSampleGenerator();
        Fuel fuelBack = getFuelRandomSampleGenerator();

        factTradeCapacity.setFuel(fuelBack);
        assertThat(factTradeCapacity.getFuel()).isEqualTo(fuelBack);

        factTradeCapacity.fuel(null);
        assertThat(factTradeCapacity.getFuel()).isNull();
    }

    @Test
    void metadataTest() {
        FactTradeCapacity factTradeCapacity = getFactTradeCapacityRandomSampleGenerator();
        Metadata metadataBack = getMetadataRandomSampleGenerator();

        factTradeCapacity.setMetadata(metadataBack);
        assertThat(factTradeCapacity.getMetadata()).isEqualTo(metadataBack);

        factTradeCapacity.metadata(null);
        assertThat(factTradeCapacity.getMetadata()).isNull();
    }
}
