package com.eidu.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RunLearningUnitResultTest {
    String contentId = "content id";
    Float score = 1.2f;
    Long foregroundDurationInMs = 123L;
    String additionalData = "additional data";
    String errorDetails = "error details";

    @Test
    public void createsResultFromSuccessIntent() {
        RunLearningUnitResult result =
                RunLearningUnitResult.ofSuccess(
                        contentId, score, foregroundDurationInMs, additionalData);

        verifyConversion(result);
    }

    @Test
    public void createsResultFromErrorIntent() {
        RunLearningUnitResult result =
                RunLearningUnitResult.ofError(
                        contentId, foregroundDurationInMs, additionalData, errorDetails);

        verifyConversion(result);
    }

    private void verifyConversion(RunLearningUnitResult result) {
        assertEquals(result, RunLearningUnitResult.fromIntent(result.toIntent()));
    }
}
