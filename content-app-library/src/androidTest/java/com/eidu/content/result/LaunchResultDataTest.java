package com.eidu.content.result;

import static org.junit.Assert.assertEquals;

import android.content.Intent;

import com.eidu.content.result.LaunchResultData.RunContentUnitResult;

import org.junit.Test;

public class LaunchResultDataTest {
    String contentId = "content id";
    Float score = 1.2f;
    Long foregroundDurationInMs = 123L;
    String additionalData = "additional data";

    @Test
    public void createLaunchDataResultFromCreatedIntentWithAdditionalData() {
        for (RunContentUnitResult runContentUnitResult : RunContentUnitResult.values()) {
            LaunchResultData launchResultData =
                    LaunchResultData.fromPlainData(
                            contentId,
                            runContentUnitResult,
                            score,
                            foregroundDurationInMs,
                            additionalData);
            Intent resultIntent = launchResultData.toResultIntent();
            LaunchResultData launchResultDataFromIntent =
                    LaunchResultData.fromResultIntent(resultIntent);

            assertEquals(
                    launchResultData.getContentId(), launchResultDataFromIntent.getContentId());
            assertEquals(
                    launchResultData.getRunContentUnitResult(),
                    launchResultDataFromIntent.getRunContentUnitResult());
            assertEquals(launchResultData.getScore(), launchResultDataFromIntent.getScore());
            assertEquals(
                    launchResultData.getForegroundDurationInMs(),
                    launchResultDataFromIntent.getForegroundDurationInMs());
            assertEquals(
                    launchResultData.getAdditionalData(),
                    launchResultDataFromIntent.getAdditionalData());
            assertEquals(launchResultData.getVersion(), launchResultDataFromIntent.getVersion());
        }
    }

    @Test
    public void createLaunchDataResultFromCreatedIntent() {
        for (RunContentUnitResult runContentUnitResult : RunContentUnitResult.values()) {
            LaunchResultData launchResultData =
                    LaunchResultData.fromPlainData(
                            contentId,
                            runContentUnitResult,
                            score,
                            foregroundDurationInMs);
            Intent resultIntent = launchResultData.toResultIntent();
            LaunchResultData launchResultDataFromIntent =
                    LaunchResultData.fromResultIntent(resultIntent);

            assertEquals(
                    launchResultData.getContentId(), launchResultDataFromIntent.getContentId());
            assertEquals(
                    launchResultData.getRunContentUnitResult(),
                    launchResultDataFromIntent.getRunContentUnitResult());
            assertEquals(launchResultData.getScore(), launchResultDataFromIntent.getScore());
            assertEquals(
                    launchResultData.getForegroundDurationInMs(),
                    launchResultDataFromIntent.getForegroundDurationInMs());
            assertEquals(
                    launchResultData.getAdditionalData(),
                    launchResultDataFromIntent.getAdditionalData());
            assertEquals(launchResultData.getVersion(), launchResultDataFromIntent.getVersion());
        }
    }
}