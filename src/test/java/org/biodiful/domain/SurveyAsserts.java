package org.biodiful.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class SurveyAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSurveyAllPropertiesEquals(Survey expected, Survey actual) {
        assertSurveyAutoGeneratedPropertiesEquals(expected, actual);
        assertSurveyAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSurveyAllUpdatablePropertiesEquals(Survey expected, Survey actual) {
        assertSurveyUpdatableFieldsEquals(expected, actual);
        assertSurveyUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSurveyAutoGeneratedPropertiesEquals(Survey expected, Survey actual) {
        assertThat(actual)
            .as("Verify Survey auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSurveyUpdatableFieldsEquals(Survey expected, Survey actual) {
        assertThat(actual)
            .as("Verify Survey relevant properties")
            .satisfies(a -> assertThat(a.getSurveyName()).as("check surveyName").isEqualTo(expected.getSurveyName()))
            .satisfies(a -> assertThat(a.getSurveyDescription()).as("check surveyDescription").isEqualTo(expected.getSurveyDescription()))
            .satisfies(a ->
                assertThat(a.getContactsDescription()).as("check contactsDescription").isEqualTo(expected.getContactsDescription())
            )
            .satisfies(a -> assertThat(a.getFriendlyURL()).as("check friendlyURL").isEqualTo(expected.getFriendlyURL()))
            .satisfies(a -> assertThat(a.getPhotoURL()).as("check photoURL").isEqualTo(expected.getPhotoURL()))
            .satisfies(a -> assertThat(a.getLogosURL()).as("check logosURL").isEqualTo(expected.getLogosURL()))
            .satisfies(a -> assertThat(a.getFormURL()).as("check formURL").isEqualTo(expected.getFormURL()))
            .satisfies(a ->
                assertThat(a.getChallengersPool1URL()).as("check challengersPool1URL").isEqualTo(expected.getChallengersPool1URL())
            )
            .satisfies(a ->
                assertThat(a.getChallengersPool2URL()).as("check challengersPool2URL").isEqualTo(expected.getChallengersPool2URL())
            )
            .satisfies(a ->
                assertThat(a.getChallengersPool3URL()).as("check challengersPool3URL").isEqualTo(expected.getChallengersPool3URL())
            )
            .satisfies(a ->
                assertThat(a.getNumberOfMatchesPerPool()).as("check numberOfMatchesPerPool").isEqualTo(expected.getNumberOfMatchesPerPool())
            )
            .satisfies(a ->
                assertThat(a.getNumberOfMatchesPerPool2())
                    .as("check numberOfMatchesPerPool2")
                    .isEqualTo(expected.getNumberOfMatchesPerPool2())
            )
            .satisfies(a ->
                assertThat(a.getNumberOfMatchesPerPool3())
                    .as("check numberOfMatchesPerPool3")
                    .isEqualTo(expected.getNumberOfMatchesPerPool3())
            )
            .satisfies(a -> assertThat(a.getMatchesDescription()).as("check matchesDescription").isEqualTo(expected.getMatchesDescription())
            )
            .satisfies(a ->
                assertThat(a.getMatchesDescriptionPool2())
                    .as("check matchesDescriptionPool2")
                    .isEqualTo(expected.getMatchesDescriptionPool2())
            )
            .satisfies(a ->
                assertThat(a.getMatchesDescriptionPool3())
                    .as("check matchesDescriptionPool3")
                    .isEqualTo(expected.getMatchesDescriptionPool3())
            )
            .satisfies(a -> assertThat(a.getOpen()).as("check open").isEqualTo(expected.getOpen()))
            .satisfies(a -> assertThat(a.getLanguage()).as("check language").isEqualTo(expected.getLanguage()))
            .satisfies(a -> assertThat(a.getUniqueChallengers()).as("check uniqueChallengers").isEqualTo(expected.getUniqueChallengers()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSurveyUpdatableRelationshipsEquals(Survey expected, Survey actual) {
        // empty method
    }
}
