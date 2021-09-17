package com.eidu.content.result;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import androidx.annotation.NonNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class LaunchResultDataTest {
    String contentId = "content id";
    Float score = 1.2f;
    Long foregroundDurationInMs = 123L;
    String additionalData = "additional data";

    @ParameterizedTest
    @EnumSource(LaunchResultData.RunContentUnitResult.class)
    public void createLaunchDataResultFromCreatedIntentWithAdditionalData(
            @NonNull LaunchResultData.RunContentUnitResult runContentUnitResult) {
        LaunchResultData launchResultData =
                LaunchResultData.fromPlainData(
                        contentId,
                        runContentUnitResult,
                        score,
                        foregroundDurationInMs,
                        additionalData);

        assertEquals(contentId, launchResultData.contentId);
        assertEquals(runContentUnitResult, launchResultData.runContentUnitResult);
        assertEquals(score, launchResultData.score);
        assertEquals(foregroundDurationInMs, launchResultData.foregroundDurationInMs);
        assertEquals(additionalData, launchResultData.additionalData);
    }

    @ParameterizedTest
    @EnumSource(LaunchResultData.RunContentUnitResult.class)
    public void createLaunchDataResultFromCreatedIntent(
            @NonNull LaunchResultData.RunContentUnitResult runContentUnitResult) {
        LaunchResultData launchResultData =
                LaunchResultData.fromPlainData(
                        contentId, runContentUnitResult, score, foregroundDurationInMs);

        assertEquals(contentId, launchResultData.contentId);
        assertEquals(runContentUnitResult, launchResultData.runContentUnitResult);
        assertEquals(score, launchResultData.score);
        assertEquals(foregroundDurationInMs, launchResultData.foregroundDurationInMs);
        assertNull(launchResultData.additionalData);
    }
}
