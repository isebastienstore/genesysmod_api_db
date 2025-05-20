package com.om4a.ct2s.domain;

import static com.om4a.ct2s.domain.CountryTestSamples.*;
import static com.om4a.ct2s.domain.FactElectricityConsumptionTestSamples.*;
import static com.om4a.ct2s.domain.MetadataTestSamples.*;
import static com.om4a.ct2s.domain.YearTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.om4a.ct2s.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FactElectricityConsumptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactElectricityConsumption.class);
        FactElectricityConsumption factElectricityConsumption1 = getFactElectricityConsumptionSample1();
        FactElectricityConsumption factElectricityConsumption2 = new FactElectricityConsumption();
        assertThat(factElectricityConsumption1).isNotEqualTo(factElectricityConsumption2);

        factElectricityConsumption2.setId(factElectricityConsumption1.getId());
        assertThat(factElectricityConsumption1).isEqualTo(factElectricityConsumption2);

        factElectricityConsumption2 = getFactElectricityConsumptionSample2();
        assertThat(factElectricityConsumption1).isNotEqualTo(factElectricityConsumption2);
    }

    @Test
    void yearTest() {
        FactElectricityConsumption factElectricityConsumption = getFactElectricityConsumptionRandomSampleGenerator();
        Year yearBack = getYearRandomSampleGenerator();

        factElectricityConsumption.setYear(yearBack);
        assertThat(factElectricityConsumption.getYear()).isEqualTo(yearBack);

        factElectricityConsumption.year(null);
        assertThat(factElectricityConsumption.getYear()).isNull();
    }

    @Test
    void countryTest() {
        FactElectricityConsumption factElectricityConsumption = getFactElectricityConsumptionRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        factElectricityConsumption.setCountry(countryBack);
        assertThat(factElectricityConsumption.getCountry()).isEqualTo(countryBack);

        factElectricityConsumption.country(null);
        assertThat(factElectricityConsumption.getCountry()).isNull();
    }

    @Test
    void metadataTest() {
        FactElectricityConsumption factElectricityConsumption = getFactElectricityConsumptionRandomSampleGenerator();
        Metadata metadataBack = getMetadataRandomSampleGenerator();

        factElectricityConsumption.setMetadata(metadataBack);
        assertThat(factElectricityConsumption.getMetadata()).isEqualTo(metadataBack);

        factElectricityConsumption.metadata(null);
        assertThat(factElectricityConsumption.getMetadata()).isNull();
    }
}
