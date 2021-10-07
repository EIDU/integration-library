package com.eidu.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RunContentUnitResultTest {
    String contentId = "content id";
    float score = 1.2f;
    long foregroundDurationInMs = 123L;
    String additionalData = "additional data";
    String errorDetails = "error details";

    @Test
    public void instantiatesForSuccess() {
        RunContentUnitResult result =
                RunContentUnitResult.ofSuccess(
                        contentId, score, foregroundDurationInMs, additionalData);

        assertEquals(contentId, result.contentId);
        assertEquals(RunContentUnitResult.ResultType.Success, result.resultType);
        assertEquals(score, result.score);
        assertEquals(foregroundDurationInMs, result.foregroundDurationInMs);
        assertEquals(additionalData, result.additionalData);
    }

    @Test
    public void instantiatesForAbort() {
        RunContentUnitResult result =
                RunContentUnitResult.ofAbort(contentId, foregroundDurationInMs, additionalData);

        assertEquals(contentId, result.contentId);
        assertEquals(RunContentUnitResult.ResultType.Abort, result.resultType);
        assertEquals(0.0, result.score);
        assertEquals(foregroundDurationInMs, result.foregroundDurationInMs);
        assertEquals(additionalData, result.additionalData);
    }

    @Test
    public void instantiatesForTimeoutInactivity() {
        RunContentUnitResult result =
                RunContentUnitResult.ofTimeoutInactivity(
                        contentId, foregroundDurationInMs, additionalData);

        assertEquals(contentId, result.contentId);
        assertEquals(RunContentUnitResult.ResultType.TimeoutInactivity, result.resultType);
        assertEquals(0.0, result.score);
        assertEquals(foregroundDurationInMs, result.foregroundDurationInMs);
        assertEquals(additionalData, result.additionalData);
    }

    @Test
    public void instantiatesForTimeUp() {
        RunContentUnitResult result =
                RunContentUnitResult.ofTimeUp(contentId, foregroundDurationInMs, additionalData);

        assertEquals(contentId, result.contentId);
        assertEquals(RunContentUnitResult.ResultType.TimeUp, result.resultType);
        assertEquals(0.0, result.score);
        assertEquals(foregroundDurationInMs, result.foregroundDurationInMs);
        assertEquals(additionalData, result.additionalData);
    }

    @Test
    public void instantiatesForError() {
        RunContentUnitResult result =
                RunContentUnitResult.ofError(
                        contentId, foregroundDurationInMs, errorDetails, additionalData);

        assertEquals(contentId, result.contentId);
        assertEquals(RunContentUnitResult.ResultType.Error, result.resultType);
        assertEquals(0.0, result.score);
        assertEquals(foregroundDurationInMs, result.foregroundDurationInMs);
        assertEquals(additionalData, result.additionalData);
        assertEquals(errorDetails, result.errorDetails);
    }
}
