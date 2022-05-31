package com.eidu.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class RunLearningUnitResultTest {
    float score = 1.2f;
    long foregroundDurationInMs = 123L;
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
    public void instantiatesForSuccess() {
        RunLearningUnitResult result =
                RunLearningUnitResult.ofSuccess(
                        score, foregroundDurationInMs, additionalData, items);

        assertEquals(RunLearningUnitResult.ResultType.Success, result.resultType);
        assertEquals(score, result.score);
        assertEquals(foregroundDurationInMs, result.foregroundDurationInMs);
        assertEquals(additionalData, result.additionalData);
        assertEquals(items, result.items);
    }

    @Test
    public void instantiatesForAbort() {
        RunLearningUnitResult result =
                RunLearningUnitResult.ofAbort(score, foregroundDurationInMs, additionalData, items);

        assertEquals(RunLearningUnitResult.ResultType.Abort, result.resultType);
        assertEquals(score, result.score);
        assertEquals(foregroundDurationInMs, result.foregroundDurationInMs);
        assertEquals(additionalData, result.additionalData);
        assertEquals(items, result.items);
    }

    @Test
    public void instantiatesForTimeoutInactivity() {
        RunLearningUnitResult result =
                RunLearningUnitResult.ofTimeoutInactivity(
                        score, foregroundDurationInMs, additionalData, items);

        assertEquals(RunLearningUnitResult.ResultType.TimeoutInactivity, result.resultType);
        assertEquals(score, result.score);
        assertEquals(foregroundDurationInMs, result.foregroundDurationInMs);
        assertEquals(additionalData, result.additionalData);
        assertEquals(items, result.items);
    }

    @Test
    public void instantiatesForTimeUp() {
        RunLearningUnitResult result =
                RunLearningUnitResult.ofTimeUp(
                        score, foregroundDurationInMs, additionalData, items);

        assertEquals(RunLearningUnitResult.ResultType.TimeUp, result.resultType);
        assertEquals(score, result.score);
        assertEquals(foregroundDurationInMs, result.foregroundDurationInMs);
        assertEquals(additionalData, result.additionalData);
        assertEquals(items, result.items);
    }

    @Test
    public void instantiatesForError() {
        RunLearningUnitResult result =
                RunLearningUnitResult.ofError(
                        null, foregroundDurationInMs, errorDetails, additionalData, items);

        assertEquals(RunLearningUnitResult.ResultType.Error, result.resultType);
        assertNull(result.score);
        assertEquals(foregroundDurationInMs, result.foregroundDurationInMs);
        assertEquals(additionalData, result.additionalData);
        assertEquals(errorDetails, result.errorDetails);
        assertEquals(items, result.items);
    }
}
