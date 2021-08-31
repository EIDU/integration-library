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

        assertEquals(contentId, launchResultData.getContentId());
        assertEquals(runContentUnitResult, launchResultData.getRunContentUnitResult());
        assertEquals(score, launchResultData.getScore());
        assertEquals(foregroundDurationInMs, launchResultData.getForegroundDurationInMs());
        assertEquals(additionalData, launchResultData.getAdditionalData());
    }

    @ParameterizedTest
    @EnumSource(LaunchResultData.RunContentUnitResult.class)
    public void createLaunchDataResultFromCreatedIntent(
            @NonNull LaunchResultData.RunContentUnitResult runContentUnitResult) {
        LaunchResultData launchResultData =
                LaunchResultData.fromPlainData(
                        contentId, runContentUnitResult, score, foregroundDurationInMs);

        assertEquals(contentId, launchResultData.getContentId());
        assertEquals(runContentUnitResult, launchResultData.getRunContentUnitResult());
        assertEquals(score, launchResultData.getScore());
        assertEquals(foregroundDurationInMs, launchResultData.getForegroundDurationInMs());
        assertNull(launchResultData.getAdditionalData());
    }
}
