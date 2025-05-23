package com.om4a.ct2s.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class TechnologyAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTechnologyAllPropertiesEquals(Technology expected, Technology actual) {
        assertTechnologyAutoGeneratedPropertiesEquals(expected, actual);
        assertTechnologyAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTechnologyAllUpdatablePropertiesEquals(Technology expected, Technology actual) {
        assertTechnologyUpdatableFieldsEquals(expected, actual);
        assertTechnologyUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTechnologyAutoGeneratedPropertiesEquals(Technology expected, Technology actual) {
        assertThat(actual)
            .as("Verify Technology auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTechnologyUpdatableFieldsEquals(Technology expected, Technology actual) {
        assertThat(actual)
            .as("Verify Technology relevant properties")
            .satisfies(a -> assertThat(a.getName()).as("check name").isEqualTo(expected.getName()))
            .satisfies(a -> assertThat(a.getCategory()).as("check category").isEqualTo(expected.getCategory()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTechnologyUpdatableRelationshipsEquals(Technology expected, Technology actual) {
        // empty method
    }
}
