package org.biodiful.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ChallengerPoolTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ChallengerPool getChallengerPoolSample1() {
        return new ChallengerPool()
            .id(1L)
            .poolOrder(1)
            .challengersURL("https://test-bucket.s3.us-east-1.amazonaws.com/survey-1/pool-1/")
            .numberOfMatches(1)
            .matchesDescription("matchesDescription1")
            .introductionMessage("introductionMessage1");
    }

    public static ChallengerPool getChallengerPoolSample2() {
        return new ChallengerPool()
            .id(2L)
            .poolOrder(2)
            .challengersURL("https://test-bucket.s3.us-east-1.amazonaws.com/survey-1/pool-2/")
            .numberOfMatches(2)
            .matchesDescription("matchesDescription2")
            .introductionMessage("introductionMessage2");
    }

    public static ChallengerPool getChallengerPoolRandomSampleGenerator() {
        return new ChallengerPool()
            .id(longCount.incrementAndGet())
            .poolOrder(intCount.incrementAndGet())
            .challengersURL(UUID.randomUUID().toString())
            .numberOfMatches(intCount.incrementAndGet())
            .matchesDescription(UUID.randomUUID().toString())
            .introductionMessage(UUID.randomUUID().toString());
    }
}
