package com.om4a.ct2s.domain;

import static com.om4a.ct2s.domain.CountryTestSamples.*;
import static com.om4a.ct2s.domain.FactTransportTestSamples.*;
import static com.om4a.ct2s.domain.MetadataTestSamples.*;
import static com.om4a.ct2s.domain.YearTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.om4a.ct2s.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FactTransportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactTransport.class);
        FactTransport factTransport1 = getFactTransportSample1();
        FactTransport factTransport2 = new FactTransport();
        assertThat(factTransport1).isNotEqualTo(factTransport2);

        factTransport2.setId(factTransport1.getId());
        assertThat(factTransport1).isEqualTo(factTransport2);

        factTransport2 = getFactTransportSample2();
        assertThat(factTransport1).isNotEqualTo(factTransport2);
    }

    @Test
    void yearTest() {
        FactTransport factTransport = getFactTransportRandomSampleGenerator();
        Year yearBack = getYearRandomSampleGenerator();

        factTransport.setYear(yearBack);
        assertThat(factTransport.getYear()).isEqualTo(yearBack);

        factTransport.year(null);
        assertThat(factTransport.getYear()).isNull();
    }

    @Test
    void countryTest() {
        FactTransport factTransport = getFactTransportRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        factTransport.setCountry(countryBack);
        assertThat(factTransport.getCountry()).isEqualTo(countryBack);

        factTransport.country(null);
        assertThat(factTransport.getCountry()).isNull();
    }

    @Test
    void metadataTest() {
        FactTransport factTransport = getFactTransportRandomSampleGenerator();
        Metadata metadataBack = getMetadataRandomSampleGenerator();

        factTransport.setMetadata(metadataBack);
        assertThat(factTransport.getMetadata()).isEqualTo(metadataBack);

        factTransport.metadata(null);
        assertThat(factTransport.getMetadata()).isNull();
    }
}
