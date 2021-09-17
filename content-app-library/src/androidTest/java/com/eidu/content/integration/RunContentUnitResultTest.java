package com.eidu.content.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import android.content.Intent;
import androidx.annotation.NonNull;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class RunContentUnitResultTest {
    String contentId = "content id";
    Float score = 1.2f;
    Long foregroundDurationInMs = 123L;
    String additionalData = "additional data";

    @ParameterizedTest
    @EnumSource(RunContentUnitResult.ResultType.class)
    public void createResultFromCreatedIntentWithAdditionalData(
            @NonNull RunContentUnitResult.ResultType type) {
        RunContentUnitResult result =
                RunContentUnitResult.fromPlainData(
                        contentId,
                        type,
                        score,
                        foregroundDurationInMs,
                        additionalData);
        Intent resultIntent = result.toResultIntent();
        RunContentUnitResult resultFromIntent = RunContentUnitResult.fromResultIntent(resultIntent);

        assertEquals(resultFromIntent, result);
    }

    @ParameterizedTest
    @EnumSource(RunContentUnitResult.ResultType.class)
    public void createResultFromCreatedIntent(
            @NonNull RunContentUnitResult.ResultType type) {
        RunContentUnitResult result =
                RunContentUnitResult.fromPlainData(contentId, type, score, foregroundDurationInMs);
        Intent resultIntent = result.toResultIntent();
        RunContentUnitResult resultFromIntent = RunContentUnitResult.fromResultIntent(resultIntent);

        assertEquals(resultFromIntent, result);
    }
}
