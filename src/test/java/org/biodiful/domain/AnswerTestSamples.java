package org.biodiful.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AnswerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Answer getAnswerSample1() {
        return new Answer()
            .id(1L)
            .judgeID("judgeID1")
            .challenger1("challenger11")
            .challenger2("challenger21")
            .winner("winner1")
            .poolNumber(1);
    }

    public static Answer getAnswerSample2() {
        return new Answer()
            .id(2L)
            .judgeID("judgeID2")
            .challenger1("challenger12")
            .challenger2("challenger22")
            .winner("winner2")
            .poolNumber(2);
    }

    public static Answer getAnswerRandomSampleGenerator() {
        return new Answer()
            .id(longCount.incrementAndGet())
            .judgeID(UUID.randomUUID().toString())
            .challenger1(UUID.randomUUID().toString())
            .challenger2(UUID.randomUUID().toString())
            .winner(UUID.randomUUID().toString())
            .poolNumber(intCount.incrementAndGet());
    }
}
