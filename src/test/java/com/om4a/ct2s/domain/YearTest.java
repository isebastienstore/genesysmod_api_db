package com.om4a.ct2s.domain;

import static com.om4a.ct2s.domain.YearTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.om4a.ct2s.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class YearTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Year.class);
        Year year1 = getYearSample1();
        Year year2 = new Year();
        assertThat(year1).isNotEqualTo(year2);

        year2.setId(year1.getId());
        assertThat(year1).isEqualTo(year2);

        year2 = getYearSample2();
        assertThat(year1).isNotEqualTo(year2);
    }
}
