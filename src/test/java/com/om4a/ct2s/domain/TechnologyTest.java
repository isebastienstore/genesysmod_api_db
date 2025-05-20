package com.om4a.ct2s.domain;

import static com.om4a.ct2s.domain.TechnologyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.om4a.ct2s.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TechnologyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Technology.class);
        Technology technology1 = getTechnologySample1();
        Technology technology2 = new Technology();
        assertThat(technology1).isNotEqualTo(technology2);

        technology2.setId(technology1.getId());
        assertThat(technology1).isEqualTo(technology2);

        technology2 = getTechnologySample2();
        assertThat(technology1).isNotEqualTo(technology2);
    }
}
