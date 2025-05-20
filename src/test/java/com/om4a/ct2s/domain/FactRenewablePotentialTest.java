package com.om4a.ct2s.domain;

import static com.om4a.ct2s.domain.CountryTestSamples.*;
import static com.om4a.ct2s.domain.FactRenewablePotentialTestSamples.*;
import static com.om4a.ct2s.domain.MetadataTestSamples.*;
import static com.om4a.ct2s.domain.TechnologyTestSamples.*;
import static com.om4a.ct2s.domain.YearTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.om4a.ct2s.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FactRenewablePotentialTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactRenewablePotential.class);
        FactRenewablePotential factRenewablePotential1 = getFactRenewablePotentialSample1();
        FactRenewablePotential factRenewablePotential2 = new FactRenewablePotential();
        assertThat(factRenewablePotential1).isNotEqualTo(factRenewablePotential2);

        factRenewablePotential2.setId(factRenewablePotential1.getId());
        assertThat(factRenewablePotential1).isEqualTo(factRenewablePotential2);

        factRenewablePotential2 = getFactRenewablePotentialSample2();
        assertThat(factRenewablePotential1).isNotEqualTo(factRenewablePotential2);
    }

    @Test
    void countryTest() {
        FactRenewablePotential factRenewablePotential = getFactRenewablePotentialRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        factRenewablePotential.setCountry(countryBack);
        assertThat(factRenewablePotential.getCountry()).isEqualTo(countryBack);

        factRenewablePotential.country(null);
        assertThat(factRenewablePotential.getCountry()).isNull();
    }

    @Test
    void yearTest() {
        FactRenewablePotential factRenewablePotential = getFactRenewablePotentialRandomSampleGenerator();
        Year yearBack = getYearRandomSampleGenerator();

        factRenewablePotential.setYear(yearBack);
        assertThat(factRenewablePotential.getYear()).isEqualTo(yearBack);

        factRenewablePotential.year(null);
        assertThat(factRenewablePotential.getYear()).isNull();
    }

    @Test
    void technologyTest() {
        FactRenewablePotential factRenewablePotential = getFactRenewablePotentialRandomSampleGenerator();
        Technology technologyBack = getTechnologyRandomSampleGenerator();

        factRenewablePotential.setTechnology(technologyBack);
        assertThat(factRenewablePotential.getTechnology()).isEqualTo(technologyBack);

        factRenewablePotential.technology(null);
        assertThat(factRenewablePotential.getTechnology()).isNull();
    }

    @Test
    void metadataTest() {
        FactRenewablePotential factRenewablePotential = getFactRenewablePotentialRandomSampleGenerator();
        Metadata metadataBack = getMetadataRandomSampleGenerator();

        factRenewablePotential.setMetadata(metadataBack);
        assertThat(factRenewablePotential.getMetadata()).isEqualTo(metadataBack);

        factRenewablePotential.metadata(null);
        assertThat(factRenewablePotential.getMetadata()).isNull();
    }
}
