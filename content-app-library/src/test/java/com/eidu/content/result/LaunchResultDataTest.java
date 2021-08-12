package com.eidu.content.result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LaunchResultDataTest {
    String contentId = "content id";
    Float score = 1.2f;
    Long foregroundDurationInMs = 123L;
    String additionalData = "additional data";

    @Test
    public void createLaunchDataResultFromCreatedIntentWithAdditionalData() {
        for (LaunchResultData.RunContentUnitResult runContentUnitResult :
                LaunchResultData.RunContentUnitResult.values()) {
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
    }

    @Test
    public void createLaunchDataResultFromCreatedIntent() {
        for (LaunchResultData.RunContentUnitResult runContentUnitResult :
                LaunchResultData.RunContentUnitResult.values()) {
            LaunchResultData launchResultData =
                    LaunchResultData.fromPlainData(
                            contentId,
                            runContentUnitResult,
                            score,
                            foregroundDurationInMs);

            assertEquals(contentId, launchResultData.getContentId());
            assertEquals(runContentUnitResult, launchResultData.getRunContentUnitResult());
            assertEquals(score, launchResultData.getScore());
            assertEquals(foregroundDurationInMs, launchResultData.getForegroundDurationInMs());
            assertNull(launchResultData.getAdditionalData());
        }
    }
}