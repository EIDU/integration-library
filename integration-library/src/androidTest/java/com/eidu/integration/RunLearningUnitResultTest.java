package com.eidu.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class RunLearningUnitResultTest {
    Float score = 1.2f;
    Long foregroundDurationInMs = 123L;
    String additionalData = "additional data";
    String errorDetails = "error details";
    List<ResultItem> items = new ArrayList<>();

    public RunLearningUnitResultTest() {
        items.add(
                new ResultItem(
                        "id1", "challenge", "givenResponse", "correctResponse", 1f, 1000L, 500L));
        items.add(
                new ResultItem(
                        "id2", "challenge", "givenResponse", "correctResponse", 0f, 0L, null));
    }

    @Test
    public void createsResultFromSuccessIntent() {
        RunLearningUnitResult result =
                RunLearningUnitResult.ofSuccess(
                        score, foregroundDurationInMs, additionalData, items);

        verifyConversion(result);
    }

    @Test
    public void createsResultFromErrorIntent() {
        RunLearningUnitResult result =
                RunLearningUnitResult.ofError(
                        score, foregroundDurationInMs, additionalData, errorDetails, null);

        verifyConversion(result);
    }

    private void verifyConversion(RunLearningUnitResult result) {
        assertEquals(result, RunLearningUnitResult.fromIntent(result.toIntent()));
    }
}
