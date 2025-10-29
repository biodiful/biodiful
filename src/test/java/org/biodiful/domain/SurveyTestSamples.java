package org.biodiful.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SurveyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Survey getSurveySample1() {
        Survey survey = new Survey()
            .id(1L)
            .surveyName("surveyName1")
            .friendlyURL("friendlyURL1")
            .photoURL("photoURL1")
            .logosURL("logosURL1")
            .formURL("formURL1");

        // Add one challenger pool
        ChallengerPool pool1 = new ChallengerPool()
            .poolOrder(1)
            .challengersURL("challengersPool1URL1")
            .numberOfMatches(10)
            .matchesDescription("matchesDescription1");
        survey.addChallengerPool(pool1);

        return survey;
    }

    public static Survey getSurveySample2() {
        Survey survey = new Survey()
            .id(2L)
            .surveyName("surveyName2")
            .friendlyURL("friendlyURL2")
            .photoURL("photoURL2")
            .logosURL("logosURL2")
            .formURL("formURL2");

        // Add two challenger pools
        ChallengerPool pool1 = new ChallengerPool()
            .poolOrder(1)
            .challengersURL("challengersPool1URL2")
            .numberOfMatches(15)
            .matchesDescription("matchesDescription2-1");
        survey.addChallengerPool(pool1);

        ChallengerPool pool2 = new ChallengerPool()
            .poolOrder(2)
            .challengersURL("challengersPool2URL2")
            .numberOfMatches(20)
            .matchesDescription("matchesDescription2-2");
        survey.addChallengerPool(pool2);

        return survey;
    }

    public static Survey getSurveyRandomSampleGenerator() {
        Survey survey = new Survey()
            .id(longCount.incrementAndGet())
            .surveyName(UUID.randomUUID().toString())
            .friendlyURL(UUID.randomUUID().toString())
            .photoURL(UUID.randomUUID().toString())
            .logosURL(UUID.randomUUID().toString())
            .formURL(UUID.randomUUID().toString());

        // Add random number of pools (1-3)
        int poolCount = random.nextInt(3) + 1;
        for (int i = 0; i < poolCount; i++) {
            ChallengerPool pool = new ChallengerPool()
                .poolOrder(i + 1)
                .challengersURL(UUID.randomUUID().toString())
                .numberOfMatches(random.nextInt(50) + 1)
                .matchesDescription(UUID.randomUUID().toString());
            survey.addChallengerPool(pool);
        }

        return survey;
    }

    public static Survey getSurveyWithMultiplePools() {
        Survey survey = new Survey()
            .id(3L)
            .surveyName("surveyName3")
            .friendlyURL("friendlyURL3")
            .photoURL("photoURL3")
            .logosURL("logosURL3")
            .formURL("formURL3");

        // Add 5 challenger pools
        for (int i = 1; i <= 5; i++) {
            ChallengerPool pool = new ChallengerPool()
                .poolOrder(i)
                .challengersURL("challengersPoolURL" + i)
                .numberOfMatches(10 + i)
                .matchesDescription("matchesDescription" + i);
            survey.addChallengerPool(pool);
        }

        return survey;
    }
}
