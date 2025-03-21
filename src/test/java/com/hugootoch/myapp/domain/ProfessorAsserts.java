package com.hugootoch.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfessorAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProfessorAllPropertiesEquals(Professor expected, Professor actual) {
        assertProfessorAutoGeneratedPropertiesEquals(expected, actual);
        assertProfessorAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProfessorAllUpdatablePropertiesEquals(Professor expected, Professor actual) {
        assertProfessorUpdatableFieldsEquals(expected, actual);
        assertProfessorUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProfessorAutoGeneratedPropertiesEquals(Professor expected, Professor actual) {
        assertThat(actual)
            .as("Verify Professor auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProfessorUpdatableFieldsEquals(Professor expected, Professor actual) {
        assertThat(actual)
            .as("Verify Professor relevant properties")
            .satisfies(a -> assertThat(a.getNome()).as("check nome").isEqualTo(expected.getNome()))
            .satisfies(a -> assertThat(a.getArea()).as("check area").isEqualTo(expected.getArea()))
            .satisfies(a -> assertThat(a.getEmail()).as("check email").isEqualTo(expected.getEmail()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProfessorUpdatableRelationshipsEquals(Professor expected, Professor actual) {
        assertThat(actual)
            .as("Verify Professor relationships")
            .satisfies(a -> assertThat(a.getTurmas()).as("check turmas").isEqualTo(expected.getTurmas()));
    }
}
