package com.eidu.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RunLearningUnitResultTest {
    float score = 1.2f;
    long foregroundDurationInMs = 123L;
    String additionalData = "additional data";
    String errorDetails = "error details";

    @Test
    public void instantiatesForSuccess() {
        RunLearningUnitResult result =
                RunLearningUnitResult.ofSuccess(score, foregroundDurationInMs, additionalData);

        assertEquals(RunLearningUnitResult.ResultType.Success, result.resultType);
        assertEquals(score, result.score);
        assertEquals(foregroundDurationInMs, result.foregroundDurationInMs);
        assertEquals(additionalData, result.additionalData);
    }

    @Test
    public void instantiatesForAbort() {
        RunLearningUnitResult result =
                RunLearningUnitResult.ofAbort(score, foregroundDurationInMs, additionalData);

        assertEquals(RunLearningUnitResult.ResultType.Abort, result.resultType);
        assertEquals(score, result.score);
        assertEquals(foregroundDurationInMs, result.foregroundDurationInMs);
        assertEquals(additionalData, result.additionalData);
    }

    @Test
    public void instantiatesForTimeoutInactivity() {
        RunLearningUnitResult result =
                RunLearningUnitResult.ofTimeoutInactivity(
                        score, foregroundDurationInMs, additionalData);

        assertEquals(RunLearningUnitResult.ResultType.TimeoutInactivity, result.resultType);
        assertEquals(score, result.score);
        assertEquals(foregroundDurationInMs, result.foregroundDurationInMs);
        assertEquals(additionalData, result.additionalData);
    }

    @Test
    public void instantiatesForTimeUp() {
        RunLearningUnitResult result =
                RunLearningUnitResult.ofTimeUp(score, foregroundDurationInMs, additionalData);

        assertEquals(RunLearningUnitResult.ResultType.TimeUp, result.resultType);
        assertEquals(score, result.score);
        assertEquals(foregroundDurationInMs, result.foregroundDurationInMs);
        assertEquals(additionalData, result.additionalData);
    }

    @Test
    public void instantiatesForError() {
        RunLearningUnitResult result =
                RunLearningUnitResult.ofError(foregroundDurationInMs, errorDetails, additionalData);

        assertEquals(RunLearningUnitResult.ResultType.Error, result.resultType);
        assertEquals(0.0, result.score);
        assertEquals(foregroundDurationInMs, result.foregroundDurationInMs);
        assertEquals(additionalData, result.additionalData);
        assertEquals(errorDetails, result.errorDetails);
    }
}
