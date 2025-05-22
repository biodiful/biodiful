package org.biodiful.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SurveyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Survey getSurveySample1() {
        return new Survey()
            .id(1L)
            .surveyName("surveyName1")
            .friendlyURL("friendlyURL1")
            .photoURL("photoURL1")
            .logosURL("logosURL1")
            .formURL("formURL1")
            .challengersPool1URL("challengersPool1URL1")
            .challengersPool2URL("challengersPool2URL1")
            .challengersPool3URL("challengersPool3URL1")
            .numberOfMatchesPerPool(1)
            .numberOfMatchesPerPool2(1)
            .numberOfMatchesPerPool3(1);
    }

    public static Survey getSurveySample2() {
        return new Survey()
            .id(2L)
            .surveyName("surveyName2")
            .friendlyURL("friendlyURL2")
            .photoURL("photoURL2")
            .logosURL("logosURL2")
            .formURL("formURL2")
            .challengersPool1URL("challengersPool1URL2")
            .challengersPool2URL("challengersPool2URL2")
            .challengersPool3URL("challengersPool3URL2")
            .numberOfMatchesPerPool(2)
            .numberOfMatchesPerPool2(2)
            .numberOfMatchesPerPool3(2);
    }

    public static Survey getSurveyRandomSampleGenerator() {
        return new Survey()
            .id(longCount.incrementAndGet())
            .surveyName(UUID.randomUUID().toString())
            .friendlyURL(UUID.randomUUID().toString())
            .photoURL(UUID.randomUUID().toString())
            .logosURL(UUID.randomUUID().toString())
            .formURL(UUID.randomUUID().toString())
            .challengersPool1URL(UUID.randomUUID().toString())
            .challengersPool2URL(UUID.randomUUID().toString())
            .challengersPool3URL(UUID.randomUUID().toString())
            .numberOfMatchesPerPool(intCount.incrementAndGet())
            .numberOfMatchesPerPool2(intCount.incrementAndGet())
            .numberOfMatchesPerPool3(intCount.incrementAndGet());
    }
}
