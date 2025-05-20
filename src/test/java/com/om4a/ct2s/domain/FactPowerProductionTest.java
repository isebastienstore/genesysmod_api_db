package com.om4a.ct2s.domain;

import static com.om4a.ct2s.domain.CountryTestSamples.*;
import static com.om4a.ct2s.domain.FactPowerProductionTestSamples.*;
import static com.om4a.ct2s.domain.FuelTestSamples.*;
import static com.om4a.ct2s.domain.MetadataTestSamples.*;
import static com.om4a.ct2s.domain.TechnologyTestSamples.*;
import static com.om4a.ct2s.domain.YearTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.om4a.ct2s.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FactPowerProductionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactPowerProduction.class);
        FactPowerProduction factPowerProduction1 = getFactPowerProductionSample1();
        FactPowerProduction factPowerProduction2 = new FactPowerProduction();
        assertThat(factPowerProduction1).isNotEqualTo(factPowerProduction2);

        factPowerProduction2.setId(factPowerProduction1.getId());
        assertThat(factPowerProduction1).isEqualTo(factPowerProduction2);

        factPowerProduction2 = getFactPowerProductionSample2();
        assertThat(factPowerProduction1).isNotEqualTo(factPowerProduction2);
    }

    @Test
    void yearTest() {
        FactPowerProduction factPowerProduction = getFactPowerProductionRandomSampleGenerator();
        Year yearBack = getYearRandomSampleGenerator();

        factPowerProduction.setYear(yearBack);
        assertThat(factPowerProduction.getYear()).isEqualTo(yearBack);

        factPowerProduction.year(null);
        assertThat(factPowerProduction.getYear()).isNull();
    }

    @Test
    void countryTest() {
        FactPowerProduction factPowerProduction = getFactPowerProductionRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        factPowerProduction.setCountry(countryBack);
        assertThat(factPowerProduction.getCountry()).isEqualTo(countryBack);

        factPowerProduction.country(null);
        assertThat(factPowerProduction.getCountry()).isNull();
    }

    @Test
    void technologyTest() {
        FactPowerProduction factPowerProduction = getFactPowerProductionRandomSampleGenerator();
        Technology technologyBack = getTechnologyRandomSampleGenerator();

        factPowerProduction.setTechnology(technologyBack);
        assertThat(factPowerProduction.getTechnology()).isEqualTo(technologyBack);

        factPowerProduction.technology(null);
        assertThat(factPowerProduction.getTechnology()).isNull();
    }

    @Test
    void fuelTest() {
        FactPowerProduction factPowerProduction = getFactPowerProductionRandomSampleGenerator();
        Fuel fuelBack = getFuelRandomSampleGenerator();

        factPowerProduction.setFuel(fuelBack);
        assertThat(factPowerProduction.getFuel()).isEqualTo(fuelBack);

        factPowerProduction.fuel(null);
        assertThat(factPowerProduction.getFuel()).isNull();
    }

    @Test
    void metadataTest() {
        FactPowerProduction factPowerProduction = getFactPowerProductionRandomSampleGenerator();
        Metadata metadataBack = getMetadataRandomSampleGenerator();

        factPowerProduction.setMetadata(metadataBack);
        assertThat(factPowerProduction.getMetadata()).isEqualTo(metadataBack);

        factPowerProduction.metadata(null);
        assertThat(factPowerProduction.getMetadata()).isNull();
    }
}
