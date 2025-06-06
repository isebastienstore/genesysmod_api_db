package com.om4a.ct2s.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class FactElectricityConsumptionAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFactElectricityConsumptionAllPropertiesEquals(
        FactElectricityConsumption expected,
        FactElectricityConsumption actual
    ) {
        assertFactElectricityConsumptionAutoGeneratedPropertiesEquals(expected, actual);
        assertFactElectricityConsumptionAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFactElectricityConsumptionAllUpdatablePropertiesEquals(
        FactElectricityConsumption expected,
        FactElectricityConsumption actual
    ) {
        assertFactElectricityConsumptionUpdatableFieldsEquals(expected, actual);
        assertFactElectricityConsumptionUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFactElectricityConsumptionAutoGeneratedPropertiesEquals(
        FactElectricityConsumption expected,
        FactElectricityConsumption actual
    ) {
        assertThat(actual)
            .as("Verify FactElectricityConsumption auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFactElectricityConsumptionUpdatableFieldsEquals(
        FactElectricityConsumption expected,
        FactElectricityConsumption actual
    ) {
        assertThat(actual)
            .as("Verify FactElectricityConsumption relevant properties")
            .satisfies(a -> assertThat(a.getValue()).as("check value").isEqualTo(expected.getValue()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFactElectricityConsumptionUpdatableRelationshipsEquals(
        FactElectricityConsumption expected,
        FactElectricityConsumption actual
    ) {
        assertThat(actual)
            .as("Verify FactElectricityConsumption relationships")
            .satisfies(a -> assertThat(a.getYear()).as("check year").isEqualTo(expected.getYear()))
            .satisfies(a -> assertThat(a.getCountry()).as("check country").isEqualTo(expected.getCountry()))
            .satisfies(a -> assertThat(a.getMetadata()).as("check metadata").isEqualTo(expected.getMetadata()));
    }
}
