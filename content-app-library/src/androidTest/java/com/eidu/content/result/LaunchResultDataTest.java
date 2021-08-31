package com.eidu.content.result;

import static org.junit.jupiter.api.Assertions.assertEquals;

import android.content.Intent;

import com.eidu.content.result.LaunchResultData.RunContentUnitResult;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class LaunchResultDataTest {
    String contentId = "content id";
    Float score = 1.2f;
    Long foregroundDurationInMs = 123L;
    String additionalData = "additional data";

    @ParameterizedTest
    @EnumSource(LaunchResultData.RunContentUnitResult.class)
    public void createLaunchDataResultFromCreatedIntentWithAdditionalData(RunContentUnitResult runContentUnitResult) {
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

    @ParameterizedTest
    @EnumSource(LaunchResultData.RunContentUnitResult.class)
    public void createLaunchDataResultFromCreatedIntent(RunContentUnitResult runContentUnitResult) {
        LaunchResultData launchResultData =
                LaunchResultData.fromPlainData(
                        contentId, runContentUnitResult, score, foregroundDurationInMs);
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
