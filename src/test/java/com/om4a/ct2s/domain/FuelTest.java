package com.om4a.ct2s.domain;

import static com.om4a.ct2s.domain.FuelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.om4a.ct2s.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FuelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fuel.class);
        Fuel fuel1 = getFuelSample1();
        Fuel fuel2 = new Fuel();
        assertThat(fuel1).isNotEqualTo(fuel2);

        fuel2.setId(fuel1.getId());
        assertThat(fuel1).isEqualTo(fuel2);

        fuel2 = getFuelSample2();
        assertThat(fuel1).isNotEqualTo(fuel2);
    }
}
