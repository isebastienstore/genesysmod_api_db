package com.om4a.ct2s.domain;

import static com.om4a.ct2s.domain.CountryTestSamples.*;
import static com.om4a.ct2s.domain.FactPowerPlantsTestSamples.*;
import static com.om4a.ct2s.domain.MetadataTestSamples.*;
import static com.om4a.ct2s.domain.TechnologyTestSamples.*;
import static com.om4a.ct2s.domain.YearTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.om4a.ct2s.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FactPowerPlantsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactPowerPlants.class);
        FactPowerPlants factPowerPlants1 = getFactPowerPlantsSample1();
        FactPowerPlants factPowerPlants2 = new FactPowerPlants();
        assertThat(factPowerPlants1).isNotEqualTo(factPowerPlants2);

        factPowerPlants2.setId(factPowerPlants1.getId());
        assertThat(factPowerPlants1).isEqualTo(factPowerPlants2);

        factPowerPlants2 = getFactPowerPlantsSample2();
        assertThat(factPowerPlants1).isNotEqualTo(factPowerPlants2);
    }

    @Test
    void commissioningDateTest() {
        FactPowerPlants factPowerPlants = getFactPowerPlantsRandomSampleGenerator();
        Year yearBack = getYearRandomSampleGenerator();

        factPowerPlants.setCommissioningDate(yearBack);
        assertThat(factPowerPlants.getCommissioningDate()).isEqualTo(yearBack);

        factPowerPlants.commissioningDate(null);
        assertThat(factPowerPlants.getCommissioningDate()).isNull();
    }

    @Test
    void decommissioningDateTest() {
        FactPowerPlants factPowerPlants = getFactPowerPlantsRandomSampleGenerator();
        Year yearBack = getYearRandomSampleGenerator();

        factPowerPlants.setDecommissioningDate(yearBack);
        assertThat(factPowerPlants.getDecommissioningDate()).isEqualTo(yearBack);

        factPowerPlants.decommissioningDate(null);
        assertThat(factPowerPlants.getDecommissioningDate()).isNull();
    }

    @Test
    void countryTest() {
        FactPowerPlants factPowerPlants = getFactPowerPlantsRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        factPowerPlants.setCountry(countryBack);
        assertThat(factPowerPlants.getCountry()).isEqualTo(countryBack);

        factPowerPlants.country(null);
        assertThat(factPowerPlants.getCountry()).isNull();
    }

    @Test
    void technologyTest() {
        FactPowerPlants factPowerPlants = getFactPowerPlantsRandomSampleGenerator();
        Technology technologyBack = getTechnologyRandomSampleGenerator();

        factPowerPlants.setTechnology(technologyBack);
        assertThat(factPowerPlants.getTechnology()).isEqualTo(technologyBack);

        factPowerPlants.technology(null);
        assertThat(factPowerPlants.getTechnology()).isNull();
    }

    @Test
    void metadataTest() {
        FactPowerPlants factPowerPlants = getFactPowerPlantsRandomSampleGenerator();
        Metadata metadataBack = getMetadataRandomSampleGenerator();

        factPowerPlants.setMetadata(metadataBack);
        assertThat(factPowerPlants.getMetadata()).isEqualTo(metadataBack);

        factPowerPlants.metadata(null);
        assertThat(factPowerPlants.getMetadata()).isNull();
    }
}
