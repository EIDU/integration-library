package com.eidu.content.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RunContentUnitResultTest {
    String contentId = "content id";
    Float score = 1.2f;
    Long foregroundDurationInMs = 123L;
    String additionalData = "additional data";

    @Test
    public void createsResultFromSuccessIntent() {
        RunContentUnitResult result = RunContentUnitResult.ofSuccess(
            contentId,
            score,
            foregroundDurationInMs,
            additionalData
        );

        verifyConversion(result);
    }

    @Test
    public void createsResultFromErrorIntent() {
        RunContentUnitResult result = RunContentUnitResult.ofError(
            contentId,
            foregroundDurationInMs,
            additionalData
        );

        verifyConversion(result);
    }

    private void verifyConversion(RunContentUnitResult result) {
        assertEquals(result, RunContentUnitResult.fromResultIntent(result.toResultIntent()));
    }
}
