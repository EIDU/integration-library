package com.eidu.content.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import androidx.annotation.NonNull;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class RunContentUnitResultTest {
    String contentId = "content id";
    float score = 1.2f;
    long foregroundDurationInMs = 123L;
    String additionalData = "additional data";

    @ParameterizedTest
    @EnumSource(RunContentUnitResult.ResultType.class)
    public void createResultFromCreatedIntentWithAdditionalData(
            @NonNull RunContentUnitResult.ResultType runContentUnitResult) {
        RunContentUnitResult result =
                RunContentUnitResult.fromPlainData(
                        contentId,
                        runContentUnitResult,
                        score,
                        foregroundDurationInMs,
                        additionalData);

        assertEquals(contentId, result.contentId);
        assertEquals(runContentUnitResult, result.resultType);
        assertEquals(score, result.score);
        assertEquals(foregroundDurationInMs, result.foregroundDurationInMs);
        assertEquals(additionalData, result.additionalData);
    }

    @ParameterizedTest
    @EnumSource(RunContentUnitResult.ResultType.class)
    public void createResultFromCreatedIntent(
            @NonNull RunContentUnitResult.ResultType runContentUnitResult) {
        RunContentUnitResult result =
                RunContentUnitResult.fromPlainData(
                        contentId, runContentUnitResult, score, foregroundDurationInMs);

        assertEquals(contentId, result.contentId);
        assertEquals(runContentUnitResult, result.resultType);
        assertEquals(score, result.score);
        assertEquals(foregroundDurationInMs, result.foregroundDurationInMs);
        assertNull(result.additionalData);
    }
}
