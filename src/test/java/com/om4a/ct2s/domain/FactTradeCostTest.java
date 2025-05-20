package com.om4a.ct2s.domain;

import static com.om4a.ct2s.domain.FactTradeCostTestSamples.*;
import static com.om4a.ct2s.domain.FuelTestSamples.*;
import static com.om4a.ct2s.domain.MetadataTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.om4a.ct2s.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FactTradeCostTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactTradeCost.class);
        FactTradeCost factTradeCost1 = getFactTradeCostSample1();
        FactTradeCost factTradeCost2 = new FactTradeCost();
        assertThat(factTradeCost1).isNotEqualTo(factTradeCost2);

        factTradeCost2.setId(factTradeCost1.getId());
        assertThat(factTradeCost1).isEqualTo(factTradeCost2);

        factTradeCost2 = getFactTradeCostSample2();
        assertThat(factTradeCost1).isNotEqualTo(factTradeCost2);
    }

    @Test
    void fuelTest() {
        FactTradeCost factTradeCost = getFactTradeCostRandomSampleGenerator();
        Fuel fuelBack = getFuelRandomSampleGenerator();

        factTradeCost.setFuel(fuelBack);
        assertThat(factTradeCost.getFuel()).isEqualTo(fuelBack);

        factTradeCost.fuel(null);
        assertThat(factTradeCost.getFuel()).isNull();
    }

    @Test
    void metadataTest() {
        FactTradeCost factTradeCost = getFactTradeCostRandomSampleGenerator();
        Metadata metadataBack = getMetadataRandomSampleGenerator();

        factTradeCost.setMetadata(metadataBack);
        assertThat(factTradeCost.getMetadata()).isEqualTo(metadataBack);

        factTradeCost.metadata(null);
        assertThat(factTradeCost.getMetadata()).isNull();
    }
}
